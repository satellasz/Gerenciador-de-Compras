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
import com.example.gerenciadorcompras.databinding.ActivityRecuperacaoContaBinding
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.singletons.AppContainer.userRepository
import com.example.gerenciadorcompras.viewmodels.RecuperacaoContaViewModel
import kotlinx.coroutines.launch

class RecuperacaoContaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecuperacaoContaBinding
    private lateinit var viewModel: RecuperacaoContaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRecuperacaoContaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = RecuperacaoContaViewModel(userRepository)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonRecuperar.setOnClickListener {
            viewModel.recuperarConta(binding.editTextEmail.text.toString())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recuperarResult.collect { result ->
                    if (result == null) return@collect

                    if (result.status == StatusResult.SALVO) {
                        Toast.makeText(
                            this@RecuperacaoContaActivity,
                            result.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@RecuperacaoContaActivity,
                            result.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}