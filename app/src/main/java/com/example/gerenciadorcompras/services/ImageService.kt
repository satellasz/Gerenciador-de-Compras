package com.example.gerenciadorcompras.services

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class ImageService(
    private val activity: AppCompatActivity,
    private val onImagePicked: (Uri) -> Unit
) {
    private var photoUri: Uri? = null

    private val requestCameraPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) openCamera() else {
                Toast.makeText(activity, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
            }
        }

    private val requestGalleryPermissionLauncher =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) openGallery() else {
                Toast.makeText(activity, "Permissão de galeria negada", Toast.LENGTH_SHORT).show()
            }
        }

    private val cameraLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                photoUri?.let { onImagePicked(it) }
            }
        }

    private val galleryLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    activity.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    photoUri = uri
                    onImagePicked(uri)
                }
            }
        }

    fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> openCamera()

            activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(activity, "A câmera é necessária para tirar foto", Toast.LENGTH_LONG)
                    .show()
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun checkGalleryPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(activity, permission) ==
                    PackageManager.PERMISSION_GRANTED -> openGallery()

            activity.shouldShowRequestPermissionRationale(permission) -> {
                Toast.makeText(
                    activity,
                    "A permissão é necessária para escolher uma imagem",
                    Toast.LENGTH_LONG
                ).show()
                requestGalleryPermissionLauncher.launch(permission)
            }

            else -> requestGalleryPermissionLauncher.launch(permission)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = File.createTempFile(
            "photo_", ".jpg",
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        photoUri = FileProvider.getUriForFile(
            activity,
            "${activity.applicationContext.packageName}.provider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }
}