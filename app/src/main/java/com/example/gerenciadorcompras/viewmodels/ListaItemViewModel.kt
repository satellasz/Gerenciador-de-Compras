package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.ViewModel
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.repositories.ItemRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ListaItemViewModel(private val repository: ItemRepository) : ViewModel() {
    private val _result = MutableSharedFlow<AppResult?>()
    val result: SharedFlow<AppResult?> get() = _result

    suspend fun updateMarcado(item: Item, isMarcado: Boolean) {
        val result = repository.updateItemMarcado(item, isMarcado)
        _result.emit(result)
    }
}