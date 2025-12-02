package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.repositories.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CriarContaViewModel(private val repository: UserRepository) : ViewModel() {
    private val _result = MutableSharedFlow<AppResult?>()
    val result: SharedFlow<AppResult?> = _result

    fun criarConta(username: String, email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            val result = repository.criarConta(username, email, password, confirmPassword)
            _result.emit(result)
        }
    }
}