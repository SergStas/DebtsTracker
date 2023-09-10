package com.sergstas.debtstracker.domain.repo

import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User

interface IDebtRepo {
    suspend fun create(debt: Debt)

    suspend fun getAll(user: User): List<Debt>
}