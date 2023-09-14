package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.DebtDao
import com.sergstas.debtstracker.data.db.models.DebtEntity.Companion.toDbEntity
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DebtRepo @Inject constructor(
    private val debtDao: DebtDao,
    private val currencyRepo: CurrencyRepo,
    private val userRepo: UserRepo,
): IDebtRepo {
    private val dispatcher = Dispatchers.IO

    override suspend fun create(debt: Debt) =
        withContext(dispatcher) {
            userRepo.create(debt.from)
            userRepo.create(debt.to)
            currencyRepo.create(debt.currency)
            val entity = debt.toDbEntity(
                fromUserId = userRepo.getIdByUserName(debt.from.username) ?: return@withContext,
                toUserId = userRepo.getIdByUserName(debt.from.username) ?: return@withContext,
                currencyId = currencyRepo.getIdByCode(debt.currency.code) ?: return@withContext,
            )
            debtDao.insert(entity)
        }

    override suspend fun getAll(user: User) =
        withContext(dispatcher) {
            debtDao.getAll().mapNotNull { it.toDomain(
                fromUser = userRepo.getById(it.fromUserId) ?: return@mapNotNull null,
                toUser = userRepo.getById(it.toUserId) ?: return@mapNotNull null,
                currency = currencyRepo.getById(it.currencyId) ?: return@mapNotNull null,
            ) }
        }
}