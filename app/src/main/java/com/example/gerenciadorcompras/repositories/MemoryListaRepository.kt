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

    override fun deleteLista(lista: Lista): Boolean {
        return try {
            AppSingleton.deleteLista(lista)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun deleteItensLista(idLista: Int): Boolean {
        return try {
            AppSingleton.deleteItensLista(idLista)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun updateLista(lista: Lista): Boolean {
        return try {
            AppSingleton.deleteLista(lista)
            AppSingleton.adicionarLista(lista)
            true
        } catch (_: Exception) {
            false
        }
    }

    override fun encontrarLista(titulo: String): Boolean {
        return AppSingleton.encontrarLista(titulo) != null
    }

    override fun encontrarLista(idLista: Int): Lista? {
        return AppSingleton.encontrarLista(idLista)
    }

    override fun getListas(): List<Lista> {
        return AppSingleton.getListas()
    }

    private fun getUltimoIdLista(): Int {
        return AppSingleton.getListas().maxOfOrNull { it.id } ?: 0
    }
}