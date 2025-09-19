package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadorcompras.models.UserResult
import com.example.gerenciadorcompras.services.LoginService

class LoginViewModel(private val service: LoginService) : ViewModel() {

    private val _loginResult = MutableLiveData<UserResult>()
    val loginResult: LiveData<UserResult> get() = _loginResult

    fun login(email: String, password: String) {
        val result = service.autenticar(email, password)
        _loginResult.value = result
    }
}