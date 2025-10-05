package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.singletons.AppSingleton

class MemoryItemRepository : ItemRepository {
    override fun adicionarItem(
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: Int,
        unidade: UnidadeItem,
        idLista: Int
    ): Boolean {
        return try {
            AppSingleton.adicionarItem(
                Item(
                    getUltimoIdItem(idLista) + 1,
                    nome, quantidade,
                    unidade,
                    categoria,
                    false,
                    idLista,
                    user
                )
            )
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun deleteItem(item: Item): Boolean {
        return try {
            AppSingleton.deleteItem(item)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun updateItem(item: Item): Boolean {
        return try {
            AppSingleton.deleteItem(item)
            AppSingleton.adicionarItem(item)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun encontrarItem(nome: String, idLista: Int): Boolean {
        return AppSingleton.encontrarItem(nome, idLista) != null
    }

    override fun encontrarItem(idItem: Int, idLista: Int): Item? {
        return AppSingleton.encontrarItem(idItem, idLista)
    }

    override fun getItens(idLista: Int): List<Item> {
        return AppSingleton.getItens(idLista)
    }

    private fun getUltimoIdItem(idLista: Int): Int {
        return AppSingleton.getItens(idLista).maxOfOrNull { it.id } ?: 0
    }
}