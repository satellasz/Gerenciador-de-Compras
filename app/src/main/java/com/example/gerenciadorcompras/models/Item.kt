package com.example.gerenciadorcompras.models

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem

data class Item(
    val id: Int,
    var nome: String,
    var quantidade: Int,
    var unidade: UnidadeItem,
    var categoria: ItemCategoria,
    var marcado: Boolean,
    val idLista: Int,
    val user: User
)
