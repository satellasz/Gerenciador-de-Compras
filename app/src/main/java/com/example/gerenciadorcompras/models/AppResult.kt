package com.example.gerenciadorcompras.models

data class AppResult(
    val success: Boolean,
    val message: String? = null,
    val userId: String? = null
)
