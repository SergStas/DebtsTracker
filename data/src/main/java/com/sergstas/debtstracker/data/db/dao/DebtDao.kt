package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.DebtEntity

@Dao
interface DebtDao {
    @Query("select * from debts where id = :id")
    suspend fun getById(id: Long): DebtEntity?

    @Query("select * from debts")
    suspend fun getAll(): List<DebtEntity>
}