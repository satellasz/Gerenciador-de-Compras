package com.example.gerenciadorcompras.datasource.user

import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.singletons.AppSingleton

class UserMemoryDataSource : UserDataSource {
    override suspend fun adicionarUser(
        id: Int,
        username: String,
        email: String,
        password: String
    ): User? {
        val user = User(id, username, email, password)
        AppSingleton.adicionarUser(user)

        return user
    }

    override suspend fun encontrarUser(
        email: String,
        password: String
    ): User? {
        return AppSingleton.encontrarUser(email, password)
    }

    override suspend fun encontrarUserPorEmail(email: String): User? {
        return AppSingleton.encontrarUserPorEmail(email)
    }

    override suspend fun getUserLogado(): User? {
        return AppSingleton.userLogado
    }

    override suspend fun setUserLogado(user: User?) {
        if (user == null) {
            AppSingleton.userLogado = null
            return
        }

        AppSingleton.userLogado = user
    }
}