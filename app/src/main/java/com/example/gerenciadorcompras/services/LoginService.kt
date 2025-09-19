package com.example.gerenciadorcompras.services

import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.models.UserResult
import com.example.gerenciadorcompras.repositories.UserRepository

class LoginService(private val userRepository: UserRepository) {
    fun autenticar(email: String, password: String): UserResult {
        if (email.isBlank() || password.isBlank()) {
            return UserResult(false)
        }
        val user = userRepository.encontrarUser(email, password)
        return if (user != null) UserResult(true, user)
        else UserResult(false)
    }

    fun login(user: User) {
        userRepository.setUserLogado(user)
    }

    fun logout() {
        userRepository.setUserLogado(null)
    }
}