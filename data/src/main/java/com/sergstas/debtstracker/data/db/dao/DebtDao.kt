package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.DebtEntity

@Dao
interface DebtDao {
    @Query("select * from debtentity where id = :id")
    suspend fun getById(id: Long): DebtEntity?

    @Query("select * from debtentity")
    suspend fun getAll(): List<DebtEntity>

    @Insert
    suspend fun insert(debtEntity: DebtEntity)
}