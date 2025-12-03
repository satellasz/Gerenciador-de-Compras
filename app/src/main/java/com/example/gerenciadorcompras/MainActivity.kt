package com.example.gerenciadorcompras

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gerenciadorcompras.databinding.ActivityMainBinding
import com.example.gerenciadorcompras.models.UserResult
import com.example.gerenciadorcompras.singletons.AppContainer.userRepository
import com.example.gerenciadorcompras.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

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

        onBackPressedDispatcher.addCallback(this) {
            logout()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = LoginViewModel(userRepository)

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

        binding.textRecuperarConta.setOnClickListener {
            val intent = Intent(this@MainActivity, RecuperacaoContaActivity::class.java)
            launcher.launch(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginResult.collect { result ->
                    if (result == null) return@collect

                    handlerLoginResult(result)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.editTextEmail.requestFocus()

        clearCampos()
    }

    private fun clearCampos() {
        binding.editTextEmail.text.clear()
        binding.editTextSenha.text.clear()
    }

    private fun logout() {
        lifecycleScope.launch {
            clearCampos()
            userRepository.logout()
        }
        moveTaskToBack(true)
    }

    private fun handlerLoginResult(result: UserResult) {
        try {
            if (result.success) {
                clearCampos()
                showToast("Login feito com sucesso")

                launcher.launch(
                    Intent(this, ListaActivity::class.java)
                )
            } else {
                showToast("Usuário/senhas inválidos")
            }
        } catch (_: FirebaseAuthInvalidUserException) {
            showToast("Usuário atualmente está indisponível no servidor")
        } catch (e: Exception) {
            showToast(e.message ?: "Erro desconhecido")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}