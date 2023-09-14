package com.sergstas.debtstracker.domain.repo

import com.sergstas.debtstracker.domain.models.Currency

interface ICurrencyRepo {
    suspend fun getAll(): List<Currency>

    suspend fun getById(id: Long): Currency?

    suspend fun getIdByCode(code: String): Long?

    suspend fun create(currency: Currency): Boolean
}