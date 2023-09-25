package com.sergstas.debtstracker.domain.repo

import com.sergstas.debtstracker.domain.models.User

interface IFriendsRepo {
    suspend fun getAll(): List<User>

    suspend fun getByGuid(guid: String): User?

    suspend fun create(user: User): Boolean
}