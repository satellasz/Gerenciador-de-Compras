package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.services.ItemService

class CriarItemViewModel(private val service: ItemService) : ViewModel() {
    private val _result = MutableLiveData<AppResult>()
    val result: LiveData<AppResult> get() = _result

    fun criarItem(
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: String,
        unidade: UnidadeItem,
        idLista: Int
    ) {
        val result = service.adicionarItem(user, nome, categoria, quantidade, unidade, idLista)
        _result.value = result
    }

    fun deletarItem(idItem: Int, idLista: Int) {
        val result = service.deleteItem(idItem, idLista)
        _result.value = result
    }

    fun updateItem(
        nome: String, categoria: ItemCategoria, quantidade: String, unidade: UnidadeItem,
        idItem: Int, idLista: Int
    ) {
        val result = service.updateItem(
            nome, categoria, quantidade, unidade,
            idItem, idLista
        )
        _result.value = result
    }
}