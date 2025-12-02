package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.datasource.item.ItemDataSource
import com.example.gerenciadorcompras.datasource.item.ItemMemoryDataSource
import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User

class ItemRepository(
    private val memory: ItemMemoryDataSource,
    private val remote: ItemDataSource
) {
    suspend fun adicionarItem(
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: String,
        unidade: UnidadeItem,
        idLista: Int
    ): AppResult {
        return try {
            if (nome.isBlank()) {
                return AppResult(
                    StatusResult.ERRO,
                    "Não é possível cadastrar um item com nome vazio"
                )
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

            val existeItem = encontrarItem(nome, idLista)

            if (existeItem != null) {
                return AppResult(StatusResult.ERRO, "Já existe um item cadastrado com este nome")
            }

            val remoteOk = remote.adicionarItem(
                0,
                user,
                nome,
                categoria,
                quantidadeInt,
                unidade,
                idLista
            )
            if (remoteOk == null) return AppResult(
                StatusResult.ERRO,
                "Erro ao salvar no servidor"
            )

            memory.adicionarItem(
                remoteOk.id,
                remoteOk.user!!,
                remoteOk.nome,
                remoteOk.categoria!!,
                quantidadeInt,
                remoteOk.unidade!!,
                remoteOk.idLista
            )

            AppResult(StatusResult.SALVO, "Item cadastrado com sucesso!")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun deleteItem(idItem: Int, idLista: Int): AppResult {
        return try {
            val item = encontrarItem(idItem, idLista)
                ?: return AppResult(StatusResult.ERRO, "Item não encontrado")

            remote.deleteItem(item)
            memory.deleteItem(item)

            AppResult(StatusResult.DELETOU, "Item excluído com sucesso")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun updateItem(
        nome: String,
        categoria: ItemCategoria,
        quantidade: String,
        unidade: UnidadeItem,
        idItem: Int,
        idLista: Int
    ): AppResult {
        return try {
            val item = encontrarItem(idItem, idLista)
                ?: return AppResult(StatusResult.ERRO, "Item não encontrado")

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

            item.nome = nome
            item.categoria = categoria
            item.quantidade = quantidadeInt
            item.unidade = unidade

            remote.updateItem(item)
            memory.updateItem(item)

            AppResult(StatusResult.EDITOU, "Item atualizado com sucesso")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun updateItemMarcado(item: Item, marcado: Boolean): AppResult {
        return try {
            item.marcado = marcado

            remote.updateItem(item)
            memory.updateItem(item)

            AppResult(StatusResult.EDITOU, "Item salvo com sucesso")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun encontrarItem(idItem: Int, idLista: Int): Item? {
        val local = memory.encontrarItem(idItem, idLista)
        if (local != null) return local

        val remoto = remote.encontrarItem(idItem, idLista)

        return remoto
    }

    suspend fun encontrarItem(nome: String, idLista: Int): Item? {
        return try {
            val local = memory.encontrarItem(nome, idLista)
            if (local != null) return local

            val remoto = remote.encontrarItem(nome, idLista)

            remoto
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getItensListaPorUsuario(user: User, idLista: Int): List<Item> {
        return getItens(idLista)
            .filter { it.user?.id == user.id }
            .sortedWith(itemSorter())
    }

    suspend fun getItensListaPorUsuario(user: User, idLista: Int, nome: String): List<Item> {
        return getItens(idLista)
            .filter { it.user?.id == user.id && it.nome.contains(nome, ignoreCase = true) }
            .sortedWith(itemSorter())
    }

    private suspend fun getItens(idLista: Int): List<Item> {
        return try {
            val itens = remote.getItens(idLista)

            memory.setLista(itens)

            itens
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun itemSorter() =
        compareBy<Item> { it.marcado }
            .thenBy { it.categoria?.nome }
            .thenBy { it.nome }
}