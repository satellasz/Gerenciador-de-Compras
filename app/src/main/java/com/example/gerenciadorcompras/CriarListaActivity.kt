package com.example.gerenciadorcompras

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gerenciadorcompras.databinding.ActivityCriarListaBinding
import com.example.gerenciadorcompras.singletons.AppContainer.listaService
import com.example.gerenciadorcompras.singletons.AppContainer.userService
import com.example.gerenciadorcompras.viewmodels.CriarListaViewModel
import java.io.File

class CriarListaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCriarListaBinding

    private lateinit var viewModel: CriarListaViewModel

    private var photoUri: Uri? = null

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            abrirCamera()
        } else {
            Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestGalleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            abrirGaleria()
        } else {
            Toast.makeText(this, "Permissão de galeria negada", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            photoUri?.let {
                binding.imageLogoLista.setImageURI(it)
            }
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            selectedImageUri?.let { uri ->
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                photoUri = uri
                binding.imageLogoLista.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCriarListaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = CriarListaViewModel(listaService)

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
                        0 -> chegarPermissaoCamera()
                        1 -> checarPermissaoGaleria()
                    }
                }
                .show()
        }

        viewModel.result.observe(this) { result ->
            if (result.success) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun chegarPermissaoCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                abrirCamera()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(
                    this,
                    "A câmera é necessária para tirar uma selfie",
                    Toast.LENGTH_LONG
                ).show()

                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun checarPermissaoGaleria() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                abrirGaleria()
            }

            shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(
                    this,
                    "A permissão é necessária para escolher uma imagem",
                    Toast.LENGTH_LONG
                ).show()

                requestGalleryPermissionLauncher.launch(permission)
            }

            else -> {
                requestGalleryPermissionLauncher.launch(permission)
            }
        }
    }

    private fun abrirCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val photoFile = File.createTempFile(
            "photo_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        photoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        cameraLauncher.launch(intent)
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }
}