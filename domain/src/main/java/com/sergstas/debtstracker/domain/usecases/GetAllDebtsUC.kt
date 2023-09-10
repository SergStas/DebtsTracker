package com.sergstas.debtstracker.domain.usecases

import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import javax.inject.Inject

class GetAllDebtsUC @Inject constructor(
    private val debtRepo: IDebtRepo,
) {
    suspend operator fun invoke(user: User) =
        debtRepo.getAll(user)
}