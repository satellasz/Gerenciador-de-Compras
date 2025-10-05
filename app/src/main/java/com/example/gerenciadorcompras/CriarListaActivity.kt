package com.example.gerenciadorcompras

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.gerenciadorcompras.databinding.ActivityCriarListaBinding
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.Lista
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

        val idLista = intent.getIntExtra("idLista", 0)

        val listaSalva = listaService.encontrarLista(idLista)

        if (listaSalva != null) {
            setupEditar(listaSalva)
        }

        viewModel = CriarListaViewModel(listaService)

        imagePicker = ImageService(this) { uri ->
            photoUri = uri
            try {
                Glide.with(this)
                    .load(uri)
                    .centerCrop()
                    .into(binding.imageLogoLista)
            } catch (ex: Throwable) {
                Toast.makeText(this, "Erro ao selecionar imagem: ${ex.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.buttonCriar.setOnClickListener {
            val uriParaSalvar = photoUri?.toString()
                ?: "android.resource://${packageName}/drawable/carrinho".toUri().toString()

            if (listaSalva != null) {
                viewModel.updateLista(
                    binding.editTextNome.text.toString(),
                    uriParaSalvar, idLista
                )
            } else {
                viewModel.criarLista(
                    userService.getUserLogado()!!,
                    binding.editTextNome.text.toString(),
                    uriParaSalvar
                )
            }
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
            when (result.status) {
                StatusResult.SALVO -> {
                    finish()
                }

                StatusResult.DELETOU, StatusResult.EDITOU -> {
                    val intent = Intent(this@CriarListaActivity, ListaActivity::class.java)
                    startActivity(intent)
                }

                else -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupEditar(lista: Lista) {
        binding.textAdicionarLista.text = this.getString(R.string.texto, "Editar Lista")
        binding.buttonCriar.text = this.getString(R.string.texto, "Salvar")
        binding.buttonExcluir.isVisible = true
        binding.buttonExcluir.isClickable = true
        binding.buttonExcluir.isEnabled = true

        binding.editTextNome.setText(lista.titulo)
        Glide.with(this)
            .load(lista.logoUri.toUri())
            .centerCrop()
            .into(binding.imageLogoLista)
        photoUri = lista.logoUri.toUri()

        binding.buttonExcluir.setOnClickListener {
            viewModel.deletarLista(lista.id)
        }
    }
}