package com.sergstas.debtstracker.domain.usecases.debts

import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import javax.inject.Inject

class GetAllDebtsUseCase @Inject constructor(
    private val debtRepo: IDebtRepo,
) {
    suspend operator fun invoke(owner: User, args: FilterArgs? = null) =
        debtRepo.getAll(owner, args)

    data class FilterArgs(
        val friends: List<User>? = null,
        val types: List<DebtTag>? = null
    ) {
        enum class DebtTag {
            All, Active, Accepted, ToPay, ToReceive, PendingConfirm, Declined;
        }
    }
}