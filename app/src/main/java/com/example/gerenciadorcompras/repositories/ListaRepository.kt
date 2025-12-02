package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.datasource.lista.ListaDataSource
import com.example.gerenciadorcompras.datasource.lista.ListaMemoryDataSource
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

class ListaRepository(
    private val memory: ListaMemoryDataSource,
    private val remote: ListaDataSource
) {
    suspend fun adicionarLista(user: User, titulo: String, logoUri: String): AppResult {
        return try {
            if (titulo.isBlank()) {
                return AppResult(StatusResult.ERRO, "Não é possível cadastrar uma lista vazia")
            }

            val existeLista = encontrarLista(titulo)

            if (existeLista != null) {
                return AppResult(
                    StatusResult.ERRO,
                    "Já existe uma lista cadastrada com este título"
                )
            }

            val successRemote = remote.adicionarLista(
                0,
                user, titulo,
                logoUri
            )

            if (successRemote == null) {
                return AppResult(StatusResult.ERRO, "Não foi possível criar a lista no servidor")
            }

            memory.adicionarLista(
                successRemote.id,
                successRemote.user!!,
                successRemote.titulo,
                successRemote.logoUri
            )

            AppResult(StatusResult.SALVO, "Lista criada com sucesso!")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun deleteLista(idLista: Int): AppResult {
        return try {
            val lista = encontrarLista(idLista)
                ?: return AppResult(StatusResult.ERRO, "Erro ao deletar a lista")

            remote.deleteItensLista(lista.id)
            memory.deleteItensLista(lista.id)

            remote.deleteLista(lista)
            memory.deleteLista(lista)

            AppResult(StatusResult.DELETOU, "Lista deletada com sucesso!")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun updateLista(titulo: String, logoUri: String, idLista: Int): AppResult {
        return try {
            if (titulo.isBlank()) {
                return AppResult(StatusResult.ERRO, "Não é possível salvar uma lista vazia")
            }

            val lista = encontrarLista(idLista)
                ?: return AppResult(StatusResult.ERRO, "Erro ao salvar lista")

            lista.titulo = titulo
            lista.logoUri = logoUri

            remote.updateLista(lista)
            memory.updateLista(lista)

            AppResult(StatusResult.EDITOU, "Lista atualizada com sucesso!")
        } catch (e: Exception) {
            AppResult(StatusResult.ERRO, e.message)
        }
    }

    suspend fun encontrarLista(idLista: Int): Lista? {
        return try {
            val local = memory.encontrarLista(idLista)
            if (local != null) return local

            val remoto = remote.encontrarLista(idLista)

            remoto
        } catch (_: Exception) {
            null
        }
    }

    suspend fun encontrarLista(titulo: String): Lista? {
        return try {
            val local = memory.encontrarLista(titulo)
            if (local != null) return local

            val remoto = remote.encontrarLista(titulo)

            remoto
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getListasPorUsuario(user: User): List<Lista> {
        val listas = getListas()
        return listas
            .filter { it.user?.id == user.id }
            .sortedBy { it.titulo }
    }

    suspend fun getListasPorUsuario(user: User, titulo: String): List<Lista> {
        val listas = getListas()
        return listas
            .filter { it.user?.id == user.id && it.titulo.contains(titulo, ignoreCase = true) }
            .sortedBy { it.titulo }
    }

    private suspend fun getListas(): List<Lista> {
        return try {
            val remoto = remote.getListas()

            memory.setLista(remoto)

            remoto
        } catch (_: Exception) {
            emptyList()
        }
    }
}
