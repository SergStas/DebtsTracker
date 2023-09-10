package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.CurrencyEntity

@Dao
interface CurrencyDao {
    @Query("select * from currencies where id = :id")
    suspend fun getById(id: Long): CurrencyEntity?
}