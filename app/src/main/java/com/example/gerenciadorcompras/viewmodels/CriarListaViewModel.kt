package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ListaRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CriarListaViewModel(private val repository: ListaRepository) : ViewModel() {
    private val _result = MutableSharedFlow<AppResult?>()
    val result: SharedFlow<AppResult?> get() = _result

    fun criarLista(user: User, titulo: String, logoUri: String) {
        viewModelScope.launch {
            val result = repository.adicionarLista(user, titulo, logoUri)
            _result.emit(result)
        }
    }

    fun deletarLista(idLista: Int) {
        viewModelScope.launch {
            val result = repository.deleteLista(idLista)
            _result.emit(result)
        }
    }

    fun updateLista(titulo: String, logoUri: String, idLista: Int) {
        viewModelScope.launch {
            val result = repository.updateLista(titulo, logoUri, idLista)
            _result.emit(result)
        }
    }

}
