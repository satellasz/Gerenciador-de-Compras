package com.example.gerenciadorcompras.models

import com.google.firebase.firestore.Exclude

data class User(
    val id: Int = 0,
    val username: String = "",
    val email: String = "",
    @get:Exclude var password: String = ""
)
