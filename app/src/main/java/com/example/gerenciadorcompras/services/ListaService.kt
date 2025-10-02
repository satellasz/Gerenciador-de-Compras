package com.example.gerenciadorcompras.services

import com.example.gerenciadorcompras.models.AppResult
import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.repositories.ListaRepository

class ListaService(private val listaRepository: ListaRepository) {
    fun adicionarLista(user: User, titulo: String, logoUri: String): AppResult {
        if (titulo.isBlank()) {
            return AppResult(false, "Não é possível cadastrar uma lista vazia")
        }

        val existeLista = listaRepository.encontrarLista(titulo)

        if (existeLista) {
            return AppResult(false, "Já existe uma lista cadastrada com este título")
        }

        val success = listaRepository.adicionarLista(user, titulo, logoUri)
        return if (success) AppResult(true, "Lista criada com sucesso!")
        else AppResult(false, "Erro ao criar a lista")
    }

    fun getListasPorUsuario(user: User): List<Lista> {
        return listaRepository.getListas().filter { it.user.id == user.id }.sortedBy { it.titulo }
    }

    fun getListasPorUsuario(user: User, titulo: String): List<Lista> {
        return listaRepository.getListas()
            .filter { it.user.id == user.id && it.titulo.contains(titulo) }.sortedBy { it.titulo }
    }
}