package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.DebtDao
import com.sergstas.debtstracker.data.db.models.DebtEntity.Companion.toDbEntity
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import com.sergstas.debtstracker.domain.usecases.debts.GetAllDebtsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class DebtRepo @Inject constructor(
    private val debtDao: DebtDao,
    private val userRepo: FriendsRepo,
    private val authRepo: AuthRepo,
): IDebtRepo {
    private val dispatcher = Dispatchers.IO

    private val mockList = listOf(
        Debt(
            owner = User("ABOBA", null, null),
            to = User("PENA", "Denis", "Petrov"),
            direction = Debt.Direction.OUTGOING,
            currency = "rub",
            sum = 54.0,
            creationDate = LocalDateTime.of(1488, 8, 22, 0, 0).toEpochSecond(ZoneOffset.UTC),
            expirationDate = null,
            description = null,
            status = Debt.Status.ACCEPTED,
        )
    )

    override suspend fun create(debt: Debt) =
        withContext(dispatcher) {
            userRepo.create(debt.owner)
            userRepo.create(debt.to)
            val entity = debt.toDbEntity()
            debtDao.insert(entity)
        }

    override suspend fun getAll(owner: User, args: GetAllDebtsUseCase.FilterArgs?) =
        withContext(dispatcher) {
            mockList.filter { it.doesMatchArgs(args) }
        }

    private suspend fun Debt.doesMatchArgs(args: GetAllDebtsUseCase.FilterArgs?) =
        args == null || (args.friends?.let {
            it.isEmpty() || to.username in it.map(User::username)
        } ?: true) && (args.types?.let { tags ->
            tags.all { tag  ->
                when (tag) {
                    GetAllDebtsUseCase.FilterArgs.DebtTag.Active -> status !in listOf(Debt.Status.DECLINED, Debt.Status.CONFIRMED)
                    GetAllDebtsUseCase.FilterArgs.DebtTag.Accepted -> status !in listOf(Debt.Status.DECLINED, Debt.Status.ASSIGNED)
                    GetAllDebtsUseCase.FilterArgs.DebtTag.ToPay -> (authRepo.getAuthedUser()?.username == owner.username) == (direction == Debt.Direction.OUTGOING)
                    GetAllDebtsUseCase.FilterArgs.DebtTag.ToReceive -> (authRepo.getAuthedUser()?.username == owner.username) != (direction == Debt.Direction.OUTGOING)
                    GetAllDebtsUseCase.FilterArgs.DebtTag.PendingConfirm -> status == Debt.Status.PAID
                    GetAllDebtsUseCase.FilterArgs.DebtTag.Declined -> status == Debt.Status.DECLINED
                }
            }
        } ?: true)
}