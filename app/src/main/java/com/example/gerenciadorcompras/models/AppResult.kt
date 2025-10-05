package com.example.gerenciadorcompras.models

import com.example.gerenciadorcompras.enums.StatusResult

data class AppResult(
    val status: StatusResult,
    val message: String? = null,
    val userId: String? = null
)
