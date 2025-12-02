package com.example.gerenciadorcompras

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gerenciadorcompras.databinding.ActivityCriarContaBinding
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.singletons.AppContainer.userRepository
import com.example.gerenciadorcompras.viewmodels.CriarContaViewModel
import kotlinx.coroutines.launch

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

        viewModel = CriarContaViewModel(userRepository)

        binding.buttonCriar.setOnClickListener {
            viewModel.criarConta(
                binding.editTextNome.text.toString(),
                binding.editTextEmail.text.toString(),
                binding.editTextSenha.text.toString(),
                binding.editTextConfirmarSenha.text.toString(),
            )
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect { result ->
                    if (result == null) return@collect

                    if (result.status == StatusResult.SALVO) {
                        Toast.makeText(this@CriarContaAcitvity, result.message, Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this@CriarContaAcitvity, result.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}