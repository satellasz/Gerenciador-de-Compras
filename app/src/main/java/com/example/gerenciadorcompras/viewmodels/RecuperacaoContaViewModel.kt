package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.repositories.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class RecuperacaoContaViewModel(private val repository: UserRepository) : ViewModel() {
    private val _recuperarResult = MutableSharedFlow<AppResult?>()
    val recuperarResult: SharedFlow<AppResult?> get() = _recuperarResult

    fun recuperarConta(email: String) {
        viewModelScope.launch {
            val result = repository.recuperarConta(email)
            _recuperarResult.emit(result)
        }
    }
}