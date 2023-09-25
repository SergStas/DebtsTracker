package com.sergstas.debtstracker.domain.usecases.debts

import com.sergstas.debtstracker.domain.models.Currency
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
        val types: List<DebtTag>? = null,
        val currencies: List<Currency>? = null,
    ) {
        enum class DebtTag {
            Active, Accepted, ToPay, ToReceive, PendingConfirm, Declined;
        }
    }
}