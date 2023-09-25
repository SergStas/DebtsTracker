package com.sergstas.debtstracker.domain.usecases.debts

import com.sergstas.debtstracker.domain.repo.IAuthRepo
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import javax.inject.Inject

class GetDebtSumUseCase @Inject constructor(
    private val debtRepo: IDebtRepo,
    private val authRepo: IAuthRepo,
) {
    suspend operator fun invoke(args: GetAllDebtsUseCase.FilterArgs? = null) =
        authRepo.getAuthedUser()?.let { user ->
            debtRepo.getAll(user, args).sumOf {
                if (it.lender.guid == user.guid) it.sum else it.sum.unaryMinus()
            }
        } ?: 0.0
}