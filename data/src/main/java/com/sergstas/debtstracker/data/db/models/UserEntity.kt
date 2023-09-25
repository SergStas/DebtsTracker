package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.User

@Entity
data class UserEntity(
    @PrimaryKey val guid: String,
    val fullName: String,
    val isReal: Boolean,
) {
    companion object {
        fun User.toDbEntity() =
            UserEntity(guid, username, isReal)
    }

    fun toDomainData() = User(guid, fullName, isReal)
}
