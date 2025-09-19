package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.models.User

interface UserRepository {
    fun adicionarUser(
        username: String,
        email: String,
        password: String
    ): Boolean

    fun encontrarUser(email: String, password: String): User?

    fun encontrarUserPorEmail(email: String): Boolean

    fun getUserLogado(): User?

    fun setUserLogado(user: User?)
}