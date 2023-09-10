package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.UserEntity

@Dao
interface UserDao {
    @Query("select * from users where id = :id")
    suspend fun getById(id: Long): UserEntity?
}