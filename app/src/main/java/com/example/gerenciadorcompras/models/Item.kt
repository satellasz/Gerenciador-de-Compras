package com.example.gerenciadorcompras.models

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem

data class Item(
    val id: Int,
    val nome: String,
    val quantidade: Int,
    val unidade: UnidadeItem,
    val categoria: ItemCategoria,
    val idLista: Int,
    val user: User
)
