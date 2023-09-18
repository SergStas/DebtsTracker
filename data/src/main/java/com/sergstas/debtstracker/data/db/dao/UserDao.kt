package com.sergstas.debtstracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sergstas.debtstracker.data.db.models.UserEntity

@Dao
interface UserDao {
    @Query("select * from userentity where username = :username")
    suspend fun getByUserName(username: String): UserEntity?

    @Query("select * from userentity")
    suspend fun getAll(): List<UserEntity>

    @Insert
    suspend fun insert(userEntity: UserEntity)
}