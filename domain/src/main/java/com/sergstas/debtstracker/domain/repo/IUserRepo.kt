package com.sergstas.debtstracker.domain.repo

import com.sergstas.debtstracker.domain.models.User

interface IUserRepo {
    suspend fun getAll(): List<User>

    suspend fun getById(id: Long): User?

    suspend fun getIdByUserName(username: String): Long?

    suspend fun create(user: User): Boolean
}