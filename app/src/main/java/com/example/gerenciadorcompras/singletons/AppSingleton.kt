package com.example.gerenciadorcompras.singletons

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

object AppSingleton {
    private val users = mutableListOf<User>()
    private val listas = mutableListOf<Lista>()

    var userLogado: User? = null

    fun adicionarUser(user: User) {
        users.add(user)
    }

    fun encontrarUser(email: String, password: String): User? {
        return users.find { it.email == email && it.password == password }
    }

    fun encontrarUserPorEmail(email: String): User? {
        return users.find { it.email == email }
    }

    fun getUsers(): List<User> {
        return users
    }

    fun adicionarLista(lista: Lista) {
        listas.add(lista)
    }

    fun encontrarLista(titulo: String): Lista? {
        return listas.find { it.titulo == titulo }
    }

    fun getListas(): List<Lista> {
        return listas
    }

}