package com.example.gerenciadorcompras.models

data class Lista(
    val id: Int,
    var titulo: String,
    var logoUri: String,
    val user: User
)
