package com.example.gerenciadorcompras.models

data class Lista(
    val id: Int = 0,
    var titulo: String = "",
    var logoUri: String = "",
    var user: User? = null
)
