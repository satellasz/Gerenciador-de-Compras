package com.example.gerenciadorcompras.datasource.lista

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

interface ListaDataSource {
    suspend fun adicionarLista(id: Int = 0, user: User, titulo: String, logoUri: String): Lista?
    suspend fun deleteLista(lista: Lista)
    suspend fun deleteItensLista(idLista: Int)
    suspend fun updateLista(lista: Lista)
    suspend fun encontrarLista(titulo: String): Lista?
    suspend fun encontrarLista(idLista: Int): Lista?
    suspend fun getListas(): List<Lista>
}