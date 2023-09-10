package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.DebtDao
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DebtRepo @Inject constructor(
    private val debtDao: DebtDao,
): IDebtRepo {
    private val dispatcher = Dispatchers.IO

    override suspend fun create(debt: Debt) =
        withContext(dispatcher) {

        }

    override suspend fun getAll(user: User) =
        withContext(dispatcher) {
            emptyList<Debt>()
        }
}