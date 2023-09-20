package com.sergstas.debtstracker.domain.usecases.debts

import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import javax.inject.Inject

class GetAllDebtsUseCase @Inject constructor(
    private val debtRepo: IDebtRepo,
) {
    suspend operator fun invoke(user: User) =
        debtRepo.getAll(user)
}