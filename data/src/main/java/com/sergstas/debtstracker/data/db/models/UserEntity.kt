package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.User

@Entity
data class UserEntity(
    val username: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    companion object {
        fun User.toDbEntity() = UserEntity(username)
    }

    fun toDomainData() = User(username)
}
