package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.services.UserService

class CriarContaViewModel(private val service: UserService) : ViewModel() {

    private val _result = MutableLiveData<AppResult>()
    val result: LiveData<AppResult> get() = _result

    fun criarConta(username: String, email: String, password: String, confirmPassword: String) {
        val result = service.criarConta(username, email, password, confirmPassword)
        _result.value = result
    }
}