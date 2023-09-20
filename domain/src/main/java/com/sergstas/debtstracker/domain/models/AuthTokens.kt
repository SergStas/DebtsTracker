package com.sergstas.debtstracker.domain.models

data class AuthTokens(
    val token: String,
    val refreshTokem: String,
)
