package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.singletons.AppSingleton

class MemoryUserRepository : UserRepository {
    override fun adicionarUser(
        username: String,
        email: String,
        password: String
    ): Boolean {
        return try {
            AppSingleton.adicionarUser(User(getUltimoIdLista() + 1, username, email, password))
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun encontrarUser(
        email: String,
        password: String
    ): User? {
        return AppSingleton.encontrarUser(email, password)
    }

    override fun encontrarUserPorEmail(email: String): Boolean {
        return AppSingleton.encontrarUserPorEmail(email) != null
    }

    override fun getUserLogado(): User? {
        return AppSingleton.userLogado
    }

    override fun setUserLogado(user: User?) {
        AppSingleton.userLogado = user
    }

    private fun getUltimoIdLista(): Int {
        return AppSingleton.getUsers().maxOfOrNull { it.id } ?: 0
    }
}