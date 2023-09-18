package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User

@Entity
data class DebtEntity(
    val fromUser: String,
    val toUser: String,
    val direction: String,
    val currency: String,
    val sum: Double,
    val creationDate: Long,
    val expirationDate: Long?,
    val description: String?,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    companion object {
        fun Debt.toDbEntity() =
            DebtEntity(
                fromUser = from.username,
                toUser = to.username,
                direction = direction.name,
                currency = currency,
                sum = sum,
                creationDate = creationDate,
                expirationDate = expirationDate,
                description = description,
            )
    }

    fun toDomain(fromUser: User, toUser: User) =
        Debt(
            from = fromUser,
            to = toUser,
            direction = Debt.Direction.valueOf(direction),
            currency = currency,
            sum = sum,
            creationDate = creationDate,
            expirationDate = expirationDate,
            description = description,
        )
}
