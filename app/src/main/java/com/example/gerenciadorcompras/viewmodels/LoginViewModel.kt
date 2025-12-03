package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.models.UserResult
import com.example.gerenciadorcompras.repositories.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableSharedFlow<UserResult?>()
    val loginResult: SharedFlow<UserResult?> get() = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.autenticar(email, password)
            _loginResult.emit(result)
        }
    }
}