package com.sergstas.debtstracker.domain.usecases.debts

import com.sergstas.debtstracker.domain.repo.IDebtRepo
import com.sergstas.debtstracker.domain.models.Debt
import javax.inject.Inject

class CreateDebtUseCase @Inject constructor(
    private val repo: IDebtRepo,
) {
    suspend operator fun invoke(debt: Debt) {
        repo.create(debt)
    }
}