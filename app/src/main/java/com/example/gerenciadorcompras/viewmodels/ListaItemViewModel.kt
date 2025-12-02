package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.repositories.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListaItemViewModel(private val repository: ItemRepository) : ViewModel() {
    private val _result = MutableStateFlow<AppResult?>(null)
    val result: StateFlow<AppResult?> get() = _result

    suspend fun updateMarcado(item: Item, isMarcado: Boolean) {
        val result = repository.updateItemMarcado(item, isMarcado)
        _result.value = result
    }
}