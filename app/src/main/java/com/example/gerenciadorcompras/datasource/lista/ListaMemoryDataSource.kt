package com.example.gerenciadorcompras.datasource.lista

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User
import com.example.gerenciadorcompras.singletons.AppSingleton

class ListaMemoryDataSource : ListaDataSource {
    override suspend fun adicionarLista(
        id: Int,
        user: User,
        titulo: String,
        logoUri: String
    ): Lista? {
        val lista = Lista(id, titulo, logoUri, user)
        AppSingleton.adicionarLista(lista)

        return lista
    }

    override suspend fun deleteLista(lista: Lista) {
        AppSingleton.deleteLista(lista)
    }

    override suspend fun deleteItensLista(idLista: Int) {
        AppSingleton.deleteItensLista(idLista)
    }

    override suspend fun updateLista(lista: Lista) {
        AppSingleton.deleteLista(lista)
        AppSingleton.adicionarLista(lista)
    }

    override suspend fun encontrarLista(titulo: String): Lista? {
        return AppSingleton.encontrarLista(titulo)
    }

    override suspend fun encontrarLista(idLista: Int): Lista? {
        return AppSingleton.encontrarLista(idLista)
    }

    override suspend fun getListas(): List<Lista> {
        return AppSingleton.getListas()
    }

    fun setLista(lista: List<Lista>) {
        AppSingleton.setLista(lista)
    }
}