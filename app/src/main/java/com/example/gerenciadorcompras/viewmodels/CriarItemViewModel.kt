package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ItemRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CriarItemViewModel(private val repository: ItemRepository) : ViewModel() {
    private val _result = MutableSharedFlow<AppResult?>()
    val result: SharedFlow<AppResult?> get() = _result

    fun criarItem(
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: String,
        unidade: UnidadeItem,
        idLista: Int
    ) {
        viewModelScope.launch {
            val result =
                repository.adicionarItem(user, nome, categoria, quantidade, unidade, idLista)
            _result.emit(result)
        }
    }

    fun deletarItem(idItem: Int, idLista: Int) {
        viewModelScope.launch {
            val result = repository.deleteItem(idItem, idLista)
            _result.emit(result)
        }
    }

    fun updateItem(
        nome: String, categoria: ItemCategoria, quantidade: String, unidade: UnidadeItem,
        idItem: Int, idLista: Int
    ) {
        viewModelScope.launch {
            val result = repository.updateItem(
                nome, categoria, quantidade, unidade,
                idItem, idLista
            )
            _result.emit(result)
        }
    }
}
