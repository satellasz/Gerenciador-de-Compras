package com.example.gerenciadorcompras.datasource.item

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User

interface ItemDataSource {
    suspend fun adicionarItem(
        id: Int,
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: Int,
        unidade: UnidadeItem,
        idLista: Int
    ): Item?

    suspend fun deleteItem(item: Item)
    suspend fun updateItem(item: Item)
    suspend fun encontrarItem(nome: String, idLista: Int): Item?
    suspend fun encontrarItem(idItem: Int, idLista: Int): Item?
    suspend fun getItens(idLista: Int): List<Item>
}