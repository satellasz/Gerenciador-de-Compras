package com.example.gerenciadorcompras.enums

import androidx.annotation.DrawableRes
import com.example.gerenciadorcompras.R

enum class ItemCategoria(
    val nome: String,
    @DrawableRes val drawableRes: Int
) {
    FRUTA("Fruta", R.drawable.fruit),
    VERDURA("Verdura", R.drawable.lettuce),
    CARNE("Carne", R.drawable.meat)
}
