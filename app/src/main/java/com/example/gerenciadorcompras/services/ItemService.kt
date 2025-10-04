package com.example.gerenciadorcompras.services

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ItemRepository

class ItemService(private val itemRepository: ItemRepository) {
    fun adicionarItem(
        user: User, nome: String, categoria: ItemCategoria, quantidade: String, unidade: UnidadeItem,
        idLista: Int
    ): AppResult {
        if (nome.isBlank()) {
            return AppResult(false, "Não é possível cadastrar um item com nome vazio")
        }

        if (quantidade.toIntOrNull() == null) {
            return AppResult(false, "Quantidade do item precisa ser um número")
        }

        val quantidadeInt = quantidade.toInt()

        if (quantidadeInt == 0) {
            return AppResult(false, "Não é possível cadastrar um item com quantidade vazia")
        }

        val existeLista = itemRepository.encontrarItem(nome, idLista)

        if (existeLista) {
            return AppResult(false, "Já existe um item cadastrado com este nome")
        }

        val success =
            itemRepository.adicionarItem(user, nome, categoria, quantidadeInt, unidade, idLista)
        return if (success) AppResult(true, "Item cadastrado com sucesso!")
        else AppResult(false, "Erro ao cadastrar o item")
    }

    fun getItensListaPorUsuario(user: User, idLista: Int): List<Item> {
        return itemRepository.getItens(idLista).filter { it.user.id == user.id }
            .sortedBy { it.nome }
    }

    fun getItensListaPorUsuario(user: User, idLista: Int, nome: String): List<Item> {
        return itemRepository.getItens(idLista)
            .filter { it.user.id == user.id && it.nome.contains(nome) }.sortedBy { it.nome }
    }
}