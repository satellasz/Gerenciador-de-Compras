package com.example.gerenciadorcompras.services

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ItemRepository

class ItemService(private val itemRepository: ItemRepository) {
    fun adicionarItem(
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: String,
        unidade: UnidadeItem,
        idLista: Int
    ): AppResult {
        if (nome.isBlank()) {
            return AppResult(StatusResult.ERRO, "Não é possível cadastrar um item com nome vazio")
        }

        if (quantidade.toIntOrNull() == null) {
            return AppResult(StatusResult.ERRO, "Quantidade do item precisa ser um número")
        }

        val quantidadeInt = quantidade.toInt()

        if (quantidadeInt == 0) {
            return AppResult(
                StatusResult.ERRO,
                "Não é possível cadastrar um item com quantidade vazia"
            )
        }

        val existeLista = itemRepository.encontrarItem(nome, idLista)

        if (existeLista) {
            return AppResult(StatusResult.ERRO, "Já existe um item cadastrado com este nome")
        }

        val success =
            itemRepository.adicionarItem(user, nome, categoria, quantidadeInt, unidade, idLista)
        return if (success) AppResult(StatusResult.SALVO, "Item cadastrado com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao cadastrar o item")
    }

    fun deleteItem(idItem: Int, idLista: Int): AppResult {
        val item = itemRepository.encontrarItem(idItem, idLista)
            ?: return AppResult(StatusResult.ERRO, "Não foi possível excluir o item")

        val success = itemRepository.deleteItem(item)
        return if (success) AppResult(StatusResult.DELETOU, "Item excluido com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao excluir o item")
    }

    fun updateItem(
        nome: String, categoria: ItemCategoria, quantidade: String, unidade: UnidadeItem,
        idItem: Int, idLista: Int
    ): AppResult {
        if (nome.isBlank()) {
            return AppResult(StatusResult.ERRO, "Não é possível salvar um item com nome vazio")
        }

        if (quantidade.toIntOrNull() == null) {
            return AppResult(StatusResult.ERRO, "Quantidade do item precisa ser um número")
        }

        val quantidadeInt = quantidade.toInt()

        if (quantidadeInt == 0) {
            return AppResult(
                StatusResult.ERRO,
                "Não é possível cadastrar um item com quantidade vazia"
            )
        }

        val item = itemRepository.encontrarItem(idItem, idLista)
            ?: return AppResult(StatusResult.ERRO, "Erro ao salvar item")

        item.nome = nome
        item.quantidade = quantidadeInt
        item.unidade = unidade
        item.categoria = categoria

        val success = itemRepository.updateItem(item)
        return if (success) AppResult(StatusResult.EDITOU, "Item salvo com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao salvar o item")
    }

    fun updateItemMarcado(
        item: Item, isMarcado: Boolean
    ): AppResult {
        item.marcado = isMarcado

        val success = itemRepository.updateItem(item)
        return if (success) AppResult(StatusResult.EDITOU, "Item salvo com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao salvar o item")
    }

    fun encontrarItem(idItem: Int, idLista: Int): Item? {
        return itemRepository.encontrarItem(idItem, idLista)
    }

    fun getItensListaPorUsuario(user: User, idLista: Int): List<Item> {
        return itemRepository.getItens(idLista).filter { it.user.id == user.id }
            .sortedWith(
                compareBy<Item> { it.marcado }
                    .thenBy { it.categoria.nome }
                    .thenBy { it.nome }
            )
    }

    fun getItensListaPorUsuario(user: User, idLista: Int, nome: String): List<Item> {
        return itemRepository.getItens(idLista)
            .filter { it.user.id == user.id && it.nome.contains(nome, ignoreCase = true) }
            .sortedWith(
                compareBy<Item> { it.marcado }
                    .thenBy { it.categoria.nome }
                    .thenBy { it.nome }
            )
    }
}