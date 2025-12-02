package com.example.gerenciadorcompras.datasource.item

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.singletons.AppSingleton

class ItemMemoryDataSource : ItemDataSource {
    override suspend fun adicionarItem(
        id: Int,
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: Int,
        unidade: UnidadeItem,
        idLista: Int
    ): Item? {
        val item = Item(
            id,
            nome, quantidade,
            unidade,
            categoria,
            false,
            idLista,
            user
        )
        AppSingleton.adicionarItem(item)

        return item;
    }

    override suspend fun deleteItem(item: Item) {
        AppSingleton.deleteItem(item)
    }

    override suspend fun updateItem(item: Item) {
        AppSingleton.deleteItem(item)
        AppSingleton.adicionarItem(item)
    }

    override suspend fun encontrarItem(nome: String, idLista: Int): Item? {
        return AppSingleton.encontrarItem(nome, idLista)
    }

    override suspend fun encontrarItem(idItem: Int, idLista: Int): Item? {
        return AppSingleton.encontrarItem(idItem, idLista)
    }

    override suspend fun getItens(idLista: Int): List<Item> {
        return AppSingleton.getItens(idLista)
    }

    fun setLista(lista: List<Item>) {
        AppSingleton.setItens(lista)
    }
}