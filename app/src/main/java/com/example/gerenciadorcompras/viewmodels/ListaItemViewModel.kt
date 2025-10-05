package com.example.gerenciadorcompras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.services.ItemService

class ListaItemViewModel(private val service: ItemService) : ViewModel() {
    private val _result = MutableLiveData<AppResult>()
    val result: LiveData<AppResult> get() = _result

    fun updateMarcado(item: Item, isMarcado: Boolean) {
        val result = service.updateItemMarcado(item, isMarcado)
        _result.value = result
    }
}