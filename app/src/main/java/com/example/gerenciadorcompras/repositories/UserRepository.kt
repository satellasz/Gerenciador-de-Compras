package com.example.gerenciadorcompras.repositories

import android.util.Patterns
import com.example.gerenciadorcompras.datasource.user.UserDataSource
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.models.UserResult
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class UserRepository(
    private val memory: UserDataSource,
    private val remote: UserDataSource
) {
    suspend fun criarConta(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): AppResult {
        return try {
            if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                return AppResult(StatusResult.ERRO, "Campos obrigatórios devem ser preenchidos")
            }

            val userExiste = encontrarUser(email)

            if (userExiste != null) {
                return AppResult(StatusResult.ERRO, "E-mail já está cadastrado")
            }

            if (password != confirmPassword) {
                return AppResult(StatusResult.ERRO, "Senhas não batem")
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return AppResult(StatusResult.ERRO, "E-mail inválido")
            }

            val successRemote = remote.adicionarUser(0, username, email, password)

            if (successRemote == null) {
                return AppResult(StatusResult.ERRO, "Não foi possível criar a conta no servidor")
            }

            memory.adicionarUser(
                successRemote.id,
                successRemote.username,
                successRemote.email,
                successRemote.password
            )

            AppResult(StatusResult.SALVO, "Conta criada com sucesso!", username)
        } catch (_: FirebaseAuthWeakPasswordException) {
            AppResult(
                StatusResult.ERRO,
                "Senha muito fraca. Necessário ter, pelo menos, 6 caracteres"
            )
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun autenticar(email: String, password: String): UserResult {
        return try {
            if (email.isBlank() || password.isBlank()) {
                return UserResult(false)
            }

            val user = encontrarUser(email, password)

            return if (user != null) {
                UserResult(true, user)
            } else UserResult(false)
        } catch (e: Exception) {
            UserResult(false, null, e.message)
        }
    }

    suspend fun encontrarUser(email: String, password: String): User? {
        val local = memory.encontrarUser(email, password)
        if (local != null) return local

        val remoto = remote.encontrarUser(email, password)

        return remoto
    }

    suspend fun encontrarUser(email: String): User? {
        return try {
            val local = memory.encontrarUserPorEmail(email)
            if (local != null) return local

            val remoto = remote.encontrarUserPorEmail(email)

            remoto
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getUserLogado(): User? {
        return try {
            val local = memory.getUserLogado()
            if (local != null) return local

            val remoto = remote.getUserLogado()
            if (remoto != null) memory.setUserLogado(remoto)

            remoto
        } catch (_: Exception) {
            null
        }
    }

    suspend fun login(user: User) {
        memory.setUserLogado(user)
        remote.setUserLogado(user)
    }

    suspend fun logout() {
        remote.setUserLogado(null)
        memory.setUserLogado(null)
    }

    suspend fun recuperarConta(email: String): AppResult {
        return try {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return AppResult(StatusResult.ERRO, "E-mail inválido")
            }

            remote.recuperarConta(email)

            AppResult(StatusResult.SALVO, "E-mail enviado com sucesso")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }

    }
}
