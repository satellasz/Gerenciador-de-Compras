package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

interface ListaRepository {
    fun adicionarLista(user: User, titulo: String, logoUri: String): Boolean
    fun deleteLista(lista: Lista): Boolean
    fun deleteItensLista(idLista: Int): Boolean
    fun updateLista(lista: Lista): Boolean
    fun encontrarLista(titulo: String): Boolean
    fun encontrarLista(idLista: Int): Lista?
    fun getListas(): List<Lista>
}