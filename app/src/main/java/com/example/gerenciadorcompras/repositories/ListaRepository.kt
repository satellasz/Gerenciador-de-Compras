package com.example.gerenciadorcompras.repositories

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

interface ListaRepository {
    fun adicionarLista(user: User, titulo: String, logoUri: String): Boolean
    fun encontrarLista(titulo: String): Boolean

    fun getListas(): List<Lista>
}