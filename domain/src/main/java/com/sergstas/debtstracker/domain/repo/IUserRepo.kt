package com.sergstas.debtstracker.domain.repo

import com.sergstas.debtstracker.domain.models.User

interface IUserRepo {
    suspend fun getAll(): List<User>

    suspend fun getByUserName(username: String): User?

    suspend fun create(user: User): Boolean
}