package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.singletons.AppSingleton

class MemoryListaRepository : ListaRepository {
    override fun adicionarLista(user: User, titulo: String, logoUri: String): Boolean {
        return try {
            AppSingleton.adicionarLista(Lista(getUltimoIdLista() + 1, titulo, logoUri, user))
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun encontrarLista(titulo: String): Boolean {
        return AppSingleton.encontrarLista(titulo) != null
    }

    override fun getListas(): List<Lista> {
        return AppSingleton.getListas()
    }

    private fun getUltimoIdLista(): Int {
        return AppSingleton.getListas().maxOfOrNull { it.id } ?: 0
    }
}