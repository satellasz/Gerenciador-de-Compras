package com.example.gerenciadorcompras.singletons

import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User

object AppSingleton {
    private val users = mutableListOf<User>()
    private val listas = mutableListOf<Lista>()

    private val itens = mutableListOf<Item>()

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

    fun setLista(lista: List<Lista>) {
        listas.clear()
        listas.addAll(lista)
    }

    fun adicionarLista(lista: Lista) {
        listas.add(lista)
    }

    fun deleteLista(lista: Lista?) {
        listas.remove(lista)
    }

    fun deleteItensLista(idLista: Int) {
        itens.removeIf { it.idLista == idLista }
    }

    fun encontrarLista(titulo: String): Lista? {
        return listas.find { it.titulo == titulo }
    }

    fun encontrarLista(idLista: Int): Lista? {
        return listas.find { it.id == idLista }
    }

    fun getListas(): List<Lista> {
        return listas
    }

    fun setItens(lista: List<Item>) {
        itens.clear()
        itens.addAll(lista)
    }

    fun adicionarItem(item: Item) {
        itens.add(item)
    }

    fun encontrarItem(nome: String, idLista: Int): Item? {
        return itens.find { it.nome == nome && it.idLista == idLista }
    }

    fun encontrarItem(idItem: Int, idLista: Int): Item? {
        return itens.find { it.id == idItem && it.idLista == idLista }
    }

    fun getItens(idLista: Int): List<Item> {
        return itens.filter { it.idLista == idLista }
    }

    fun deleteItem(item: Item?) {
        itens.remove(item)
    }

}