package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.User

@Entity
data class UserEntity(
    @PrimaryKey val username: String,
    val firstName: String?,
    val lastName: String?,
    val hasAvatar: Boolean = false,
) {
    companion object {
        fun User.toDbEntity() =
            UserEntity(username, firstName, lastName, hasAvatar)
    }

    fun toDomainData() = User(username, firstName, lastName, hasAvatar)
}
