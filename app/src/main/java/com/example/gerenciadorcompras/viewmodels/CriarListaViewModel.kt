package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.services.ListaService

class CriarListaViewModel(private val service: ListaService) : ViewModel() {

    private val _result = MutableLiveData<AppResult>()
    val result: LiveData<AppResult> get() = _result

    fun criarLista(user: User, titulo: String, logoUri: String) {
        val result = service.adicionarLista(user, titulo, logoUri)
        _result.value = result
    }
}