package com.example.gerenciadorcompras.singletons

import com.example.gerenciadorcompras.repositories.MemoryItemRepository
import com.example.gerenciadorcompras.repositories.MemoryListaRepository
import com.example.gerenciadorcompras.repositories.MemoryUserRepository
import com.example.gerenciadorcompras.services.ItemService
import com.example.gerenciadorcompras.services.ListaService
import com.example.gerenciadorcompras.services.LoginService
import com.example.gerenciadorcompras.services.UserService

object AppContainer {
    val userRepository = MemoryUserRepository()
    val listaRepository = MemoryListaRepository()
    val itemRepository = MemoryItemRepository()
    val userService = UserService(userRepository)
    val listaService = ListaService(listaRepository)
    val loginService = LoginService(userRepository)

    val itemService = ItemService(itemRepository)
}