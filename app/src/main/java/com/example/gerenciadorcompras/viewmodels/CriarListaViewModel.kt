package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ListaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CriarListaViewModel(private val repository: ListaRepository) : ViewModel() {
    private val _result = MutableStateFlow<AppResult?>(null)
    val result: StateFlow<AppResult?> get() = _result

    fun criarLista(user: User, titulo: String, logoUri: String) {
        viewModelScope.launch {
            val result = repository.adicionarLista(user, titulo, logoUri)
            _result.value = result
        }
    }

    fun deletarLista(idLista: Int) {
        viewModelScope.launch {
            val result = repository.deleteLista(idLista)
            _result.value = result
        }
    }

    fun updateLista(titulo: String, logoUri: String, idLista: Int) {
        viewModelScope.launch {
            val result = repository.updateLista(titulo, logoUri, idLista)
            _result.value = result
        }
    }

    fun carregarLista(id: Int) {
        viewModelScope.launch {
            val result = repository.encontrarLista(id)
            _result.value = AppResult(StatusResult.SALVO)
        }
    }
}
