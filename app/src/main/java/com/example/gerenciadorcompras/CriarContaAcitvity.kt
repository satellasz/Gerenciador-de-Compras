package com.example.gerenciadorcompras

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gerenciadorcompras.databinding.ActivityCriarContaBinding
import com.example.gerenciadorcompras.singletons.AppContainer.userService
import com.example.gerenciadorcompras.viewmodels.CriarContaViewModel

class CriarContaAcitvity : AppCompatActivity() {
    private lateinit var binding: ActivityCriarContaBinding
    private lateinit var viewModel: CriarContaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCriarContaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.criarConta) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        viewModel = CriarContaViewModel(userService)

        binding.buttonCriar.setOnClickListener {
            viewModel.criarConta(
                binding.editTextNome.text.toString(),
                binding.editTextEmail.text.toString(),
                binding.editTextSenha.text.toString(),
                binding.editTextConfirmarSenha.text.toString(),
            )
        }

        viewModel.result.observe(this) { result ->
            if (result.success) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}