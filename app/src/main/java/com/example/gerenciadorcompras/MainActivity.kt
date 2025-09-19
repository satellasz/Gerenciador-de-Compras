package com.example.gerenciadorcompras

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gerenciadorcompras.databinding.ActivityMainBinding
import com.example.gerenciadorcompras.repositories.MemoryUserRepository
import com.example.gerenciadorcompras.services.LoginService
import com.example.gerenciadorcompras.viewmodels.LoginViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginViewModel

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val activityTarget = result.data?.component?.className
            Log.i(
                activityTarget,
                "Sucesso ao trocar da Activity: " + this@MainActivity.localClassName + " para a Activity" + activityTarget
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = MemoryUserRepository()
        val service = LoginService(repository)

        viewModel = LoginViewModel(service)

        binding.buttonAcessar.setOnClickListener {
            viewModel.login(
                binding.editTextEmail.text.toString(),
                binding.editTextSenha.text.toString()
            )
        }

        binding.buttonCriarConta.setOnClickListener {
            val intent = Intent(this@MainActivity, CriarContaAcitvity::class.java)
            launcher.launch(intent)
        }

        viewModel.loginResult.observe(this) { success ->
            if (success.success) {
                service.login(success.user!!)
                clearCampos()
                Toast.makeText(this, "Login feito com sucesso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, ListaActivity::class.java)
                launcher.launch(intent)
            } else {
                Toast.makeText(this, "Usuário/senhas inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.editTextEmail.requestFocus()

        clearCampos()
    }

    private fun clearCampos() {
        binding.editTextEmail.getText().clear()
        binding.editTextSenha.getText().clear()
    }
}