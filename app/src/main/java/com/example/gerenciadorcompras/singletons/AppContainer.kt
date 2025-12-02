package com.example.gerenciadorcompras.singletons

import com.example.gerenciadorcompras.datasource.item.ItemFireBaseDataSource
import com.example.gerenciadorcompras.datasource.item.ItemMemoryDataSource
import com.example.gerenciadorcompras.datasource.lista.ListaFireBaseDataSource
import com.example.gerenciadorcompras.datasource.lista.ListaMemoryDataSource
import com.example.gerenciadorcompras.datasource.user.UserFireBaseDataSource
import com.example.gerenciadorcompras.datasource.user.UserMemoryDataSource
import com.example.gerenciadorcompras.repositories.ItemRepository
import com.example.gerenciadorcompras.repositories.ListaRepository
import com.example.gerenciadorcompras.repositories.UserRepository

object AppContainer {
    // User
    val userMemoryDataSource = UserMemoryDataSource()
    val userFireBaseDataSource = UserFireBaseDataSource()
    val userRepository = UserRepository(userMemoryDataSource, userFireBaseDataSource)

    // Lista
    val listaMemoryDataSource = ListaMemoryDataSource()
    val listaFireBaseDataSource = ListaFireBaseDataSource()
    val listaRepository = ListaRepository(listaMemoryDataSource, listaFireBaseDataSource)

    // Item
    val itemMemoryDataSource = ItemMemoryDataSource()
    val itemFireBaseDataSource = ItemFireBaseDataSource()
    val itemRepository = ItemRepository(itemMemoryDataSource, itemFireBaseDataSource)
}