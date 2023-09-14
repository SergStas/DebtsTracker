package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.CurrencyDao
import com.sergstas.debtstracker.data.db.models.CurrencyEntity
import com.sergstas.debtstracker.data.db.models.CurrencyEntity.Companion.toDbEntity
import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.domain.repo.ICurrencyRepo
import javax.inject.Inject

class CurrencyRepo @Inject constructor(
    private val currencyDao: CurrencyDao,
): ICurrencyRepo {
    override suspend fun getAll() =
        currencyDao.getAll().map(CurrencyEntity::toDomainData)

    override suspend fun getById(id: Long) =
        currencyDao.getById(id)?.toDomainData()

    override suspend fun getIdByCode(code: String) =
        currencyDao.getByCode(code)?.id

    override suspend fun create(currency: Currency): Boolean {
        val existing = currencyDao.getAll()
        return if (currency.code.lowercase() in existing.map { it.code.lowercase() }) {
            false
        } else {
            currencyDao.insert(currency.toDbEntity())
            true
        }
    }
}