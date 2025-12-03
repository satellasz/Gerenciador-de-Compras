package com.example.gerenciadorcompras.models

data class UserResult(
    val success: Boolean,
    val user: User? = null,
    val mensagem: String? = null
)
