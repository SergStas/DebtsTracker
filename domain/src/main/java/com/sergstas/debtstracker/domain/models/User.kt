package com.sergstas.debtstracker.domain.models

data class User(
    val username: String,
    val firstName: String?,
    val lastName: String?,
    val hasAvatar: Boolean = false,
)
