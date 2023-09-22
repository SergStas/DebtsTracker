package com.sergstas.debtstracker.domain.repo

import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.usecases.debts.GetAllDebtsUseCase

interface IDebtRepo {
    suspend fun create(debt: Debt)

    suspend fun getAll(owner: User, args: GetAllDebtsUseCase.FilterArgs? = null): List<Debt>
}