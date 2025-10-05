package com.example.gerenciadorcompras.services

import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ListaRepository

class ListaService(private val listaRepository: ListaRepository) {
    fun adicionarLista(user: User, titulo: String, logoUri: String): AppResult {
        if (titulo.isBlank()) {
            return AppResult(StatusResult.ERRO, "Não é possível cadastrar uma lista vazia")
        }

        val existeLista = listaRepository.encontrarLista(titulo)

        if (existeLista) {
            return AppResult(StatusResult.ERRO, "Já existe uma lista cadastrada com este título")
        }

        val success = listaRepository.adicionarLista(user, titulo, logoUri)
        return if (success) AppResult(StatusResult.DELETOU, "Lista criada com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao criar a lista")
    }

    fun deleteLista(idLista: Int): AppResult {
        val lista = listaRepository.encontrarLista(idLista)
            ?: return AppResult(StatusResult.ERRO, "Erro ao deletar a lista")

        val successItens = listaRepository.deleteItensLista(lista.id)

        if (!successItens) {
            return AppResult(StatusResult.ERRO, "Erro ao deletar os itens da lista")
        }

        val success = listaRepository.deleteLista(lista)
        return if (success) AppResult(StatusResult.DELETOU, "Lista deletada com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao deletar a lista")
    }

    fun updateLista(titulo: String, logoUri: String, idLista: Int): AppResult {
        if (titulo.isBlank()) {
            return AppResult(StatusResult.ERRO, "Não é possível salvar uma lista vazia")
        }

        val lista = listaRepository.encontrarLista(idLista)
            ?: return AppResult(StatusResult.ERRO, "Erro ao salvar lista")

        lista.titulo = titulo
        lista.logoUri = logoUri

        val success = listaRepository.updateLista(lista)
        return if (success) AppResult(StatusResult.EDITOU, "Lista criada com sucesso!")
        else AppResult(StatusResult.ERRO, "Erro ao criar a lista")
    }

    fun encontrarLista(idLista: Int): Lista? {
        return listaRepository.encontrarLista(idLista)
    }

    fun getListasPorUsuario(user: User): List<Lista> {
        return listaRepository.getListas().filter { it.user.id == user.id }.sortedBy { it.titulo }
    }

    fun getListasPorUsuario(user: User, titulo: String): List<Lista> {
        return listaRepository.getListas()
            .filter { it.user.id == user.id && it.titulo.contains(titulo, ignoreCase = true) }
            .sortedBy { it.titulo }
    }
}