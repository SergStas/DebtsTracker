package com.sergstas.debtstracker.domain.models

data class Debt(
    val from: User,
    val to: User,
    val currency: String,
    val sum: Double,
    val creationDate: Long,
    val expirationDate: Long,
    val description: String,
)
