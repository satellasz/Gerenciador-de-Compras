package com.example.gerenciadorcompras.datasource.user

import com.example.gerenciadorcompras.models.User

interface UserDataSource {
    suspend fun adicionarUser(
        id: Int = 0,
        username: String,
        email: String,
        password: String
    ): User?

    suspend fun encontrarUser(email: String, password: String): User?

    suspend fun encontrarUserPorEmail(email: String): User?

    suspend fun getUserLogado(): User?

    suspend fun setUserLogado(user: User?)

    suspend fun recuperarConta(email: String)
}