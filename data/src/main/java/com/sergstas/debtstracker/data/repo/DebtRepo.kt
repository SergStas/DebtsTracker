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
    private val userRepo: UserRepo,
): IDebtRepo {
    private val dispatcher = Dispatchers.IO

    override suspend fun create(debt: Debt) =
        withContext(dispatcher) {
            userRepo.create(debt.from)
            userRepo.create(debt.to)
            val entity = debt.toDbEntity()
            debtDao.insert(entity)
        }

    override suspend fun getAll(user: User) =
        withContext(dispatcher) {
            debtDao.getAll().mapNotNull { it.toDomain(
                fromUser = userRepo.getByUserName(it.fromUser) ?: return@mapNotNull null,
                toUser = userRepo.getByUserName(it.toUser) ?: return@mapNotNull null,
            ) }
        }
}