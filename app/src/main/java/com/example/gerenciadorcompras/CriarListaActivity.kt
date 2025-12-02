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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.gerenciadorcompras.databinding.ActivityCriarListaBinding
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.services.ImageService
import com.example.gerenciadorcompras.singletons.AppContainer.listaRepository
import com.example.gerenciadorcompras.singletons.AppContainer.userRepository
import com.example.gerenciadorcompras.viewmodels.CriarListaViewModel
import kotlinx.coroutines.launch

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

        viewModel = CriarListaViewModel(listaRepository)

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

        lifecycleScope.launch {
            if (idLista != 0) {
                val listaSalva = listaRepository.encontrarLista(idLista)

                if (listaSalva != null) {
                    setupEditar(listaSalva)
                }
            }
        }

        setupBotaoCriar(idLista)

        setupBotaoCam()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.result.collect { result ->
                    if (result == null) return@collect

                    Toast.makeText(this@CriarListaActivity, result.message, Toast.LENGTH_SHORT)
                        .show()
                    when (result.status) {
                        StatusResult.SALVO -> {
                            finish()
                        }

                        StatusResult.DELETOU, StatusResult.EDITOU -> {
                            val intent = Intent(this@CriarListaActivity, ListaActivity::class.java)
                            startActivity(intent)
                        }

                        else -> {
                            Toast.makeText(
                                this@CriarListaActivity,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
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

    private fun setupBotaoCriar(idLista: Int) {
        binding.buttonCriar.setOnClickListener {
            val uriParaSalvar = photoUri?.toString()
                ?: "android.resource://${packageName}/drawable/carrinho".toUri().toString()
            lifecycleScope.launch {
                if (idLista != 0 && listaRepository.encontrarLista(idLista) != null) {
                    viewModel.updateLista(
                        binding.editTextNome.text.toString(),
                        uriParaSalvar, idLista
                    )
                } else {
                    viewModel.criarLista(
                        userRepository.getUserLogado()!!,
                        binding.editTextNome.text.toString(),
                        uriParaSalvar
                    )
                }
            }
        }
    }

    private fun setupBotaoCam() {
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
    }
}