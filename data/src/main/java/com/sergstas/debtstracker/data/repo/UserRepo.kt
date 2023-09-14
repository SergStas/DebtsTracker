package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.db.dao.UserDao
import com.sergstas.debtstracker.data.db.models.UserEntity
import com.sergstas.debtstracker.data.db.models.UserEntity.Companion.toDbEntity
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IUserRepo
import javax.inject.Inject

class UserRepo @Inject constructor(
    private val userDao: UserDao,
): IUserRepo {
    override suspend fun getAll() =
        userDao.getAll().map(UserEntity::toDomainData)

    override suspend fun getById(id: Long) =
        userDao.getById(id)?.toDomainData()

    override suspend fun getIdByUserName(username: String) =
        userDao.getByUserName(username)?.id

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