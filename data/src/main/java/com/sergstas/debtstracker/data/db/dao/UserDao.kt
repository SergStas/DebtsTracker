package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.UserEntity

@Dao
interface UserDao {
    @Query("select * from userentity where guid = :guid")
    suspend fun getByGuid(guid: String): UserEntity?

    @Query("select * from userentity")
    suspend fun getAll(): List<UserEntity>

    @Insert
    suspend fun insert(userEntity: UserEntity)
}