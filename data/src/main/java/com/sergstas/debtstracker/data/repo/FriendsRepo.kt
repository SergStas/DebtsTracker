package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.UserDao
import com.sergstas.debtstracker.data.db.models.UserEntity
import com.sergstas.debtstracker.data.db.models.UserEntity.Companion.toDbEntity
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IFriendsRepo
import javax.inject.Inject

class FriendsRepo @Inject constructor(
    private val userDao: UserDao,
): IFriendsRepo {
    override suspend fun getAll() =
        userDao.getAll().map(UserEntity::toDomainData)

    override suspend fun getByUserName(username: String) =
        userDao.getByUserName(username)?.toDomainData()

    override suspend fun create(user: User): Boolean {
        val existing = userDao.getAll()
        return if (user.username in existing.map { it.username }) {
            false
        } else {
            userDao.insert(user.toDbEntity())
            true
        }
    }
}