package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.CurrencyEntity

@Dao
interface CurrencyDao {
    @Query("select * from currencyentity where id = :id")
    suspend fun getById(id: Long): CurrencyEntity?

    @Query("select * from currencyentity")
    suspend fun getAll(): List<CurrencyEntity>

    @Query("select * from currencyentity where code = :code")
    suspend fun getByCode(code: String): CurrencyEntity?

    @Insert
    suspend fun insert(currencyEntity: CurrencyEntity)
}