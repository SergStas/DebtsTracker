package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.UserDao
import com.sergstas.debtstracker.data.db.models.UserEntity.Companion.toDbEntity
import com.sergstas.debtstracker.data.repo.FriendsRepo.UsersMock.me
import com.sergstas.debtstracker.data.repo.FriendsRepo.UsersMock.pena
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IFriendsRepo
import javax.inject.Inject

class FriendsRepo @Inject constructor(
    private val userDao: UserDao,
): IFriendsRepo {
    object UsersMock {
        val me = User("1", "aboba", true)
        val pena = User("2", "Denis Petrov", true)
    }

    override suspend fun getAll() =
        listOf(me, pena)

    override suspend fun getByGuid(guid: String) =
        userDao.getByGuid(guid)?.toDomainData()

    override suspend fun create(user: User): Boolean {
        val existing = userDao.getAll()
        return if (user.guid in existing.map { it.guid }) {
            false
        } else {
            userDao.insert(user.toDbEntity())
            true
        }
    }
}