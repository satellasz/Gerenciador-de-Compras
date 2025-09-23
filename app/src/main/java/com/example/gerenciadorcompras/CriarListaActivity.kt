package com.example.gerenciadorcompras

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.gerenciadorcompras.databinding.ActivityCriarListaBinding
import com.example.gerenciadorcompras.services.ImageService
import com.example.gerenciadorcompras.singletons.AppContainer.listaService
import com.example.gerenciadorcompras.singletons.AppContainer.userService
import com.example.gerenciadorcompras.viewmodels.CriarListaViewModel

class CriarListaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCriarListaBinding
    private lateinit var viewModel: CriarListaViewModel
    private lateinit var imagePicker: ImageService

    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCriarListaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel = CriarListaViewModel(listaService)

        imagePicker = ImageService(this) { uri ->
            photoUri = uri
            binding.imageLogoLista.setImageURI(uri)
        }

        binding.buttonCriar.setOnClickListener {
            val uriParaSalvar = photoUri?.toString()
                ?: "android.resource://${packageName}/drawable/carrinho".toUri().toString()

            viewModel.criarLista(
                userService.getUserLogado()!!,
                binding.editTextNome.text.toString(),
                uriParaSalvar
            )
        }

        binding.fabCam.setOnClickListener {
            val options = arrayOf("Tirar Foto", "Escolher da Galeria")

            AlertDialog.Builder(this)
                .setTitle("Selecionar Imagem")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> imagePicker.checkCameraPermission()
                        1 -> imagePicker.checkGalleryPermission()
                    }
                }
                .show()
        }

        viewModel.result.observe(this) { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            if (result.success) {
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}