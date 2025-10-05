package com.example.gerenciadorcompras.services

import android.util.Patterns
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.UserRepository

class UserService(private val userRepository: UserRepository) {
    fun criarConta(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): AppResult {
        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return AppResult(StatusResult.ERRO, "Campos obrigatórios devem ser preenchidos")
        }

        val userExiste = userRepository.encontrarUserPorEmail(email)

        if (userExiste) {
            return AppResult(StatusResult.ERRO, "E-mail já está cadastrado")
        }

        if (password != confirmPassword) {
            return AppResult(StatusResult.ERRO, "Senhas não batem")
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return AppResult(StatusResult.ERRO, "E-mail inválido")
        }

        val success = userRepository.adicionarUser(username, email, password)
        return if (success) AppResult(StatusResult.SALVO, "Conta criada com sucesso!", username)
        else AppResult(StatusResult.ERRO, "Erro ao criar a conta")
    }

    fun getUserLogado(): User? {
        return userRepository.getUserLogado()
    }

    fun setUserLogado(user: User) {
        userRepository.setUserLogado(user)
    }
}