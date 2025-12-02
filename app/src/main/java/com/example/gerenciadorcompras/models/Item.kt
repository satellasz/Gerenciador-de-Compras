package com.example.gerenciadorcompras.models

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem

data class Item(
    val id: Int = 0,
    var nome: String = "",
    var quantidade: Int = 0,
    var unidade: UnidadeItem? = null,
    var categoria: ItemCategoria? = null,
    var marcado: Boolean = false,
    val idLista: Int = 0,
    val user: User? = null
)
