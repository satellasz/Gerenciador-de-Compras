package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

interface ItemRepository {
    fun adicionarItem(
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: Int,
        unidade: UnidadeItem,
        idLista: Int
    ): Boolean

    fun deleteItem(item: Item): Boolean

    fun updateItem(item: Item): Boolean

    fun encontrarItem(nome: String, idLista: Int): Boolean
    fun encontrarItem(idItem: Int, idLista: Int): Item?

    fun getItens(idLista: Int): List<Item>
}