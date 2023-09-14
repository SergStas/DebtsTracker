package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User

@Entity
data class DebtEntity(
    val fromUserId: Long,
    val toUserId: Long,
    val currencyId: Long,
    val sum: Double,
    val creationDate: Long,
    val expirationDate: Long,
    val description: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    companion object {
        fun Debt.toDbEntity(fromUserId: Long, toUserId: Long, currencyId: Long) =
            DebtEntity(
                fromUserId = fromUserId,
                toUserId = toUserId,
                currencyId = currencyId,
                sum = sum,
                creationDate = creationDate,
                expirationDate = expirationDate,
                description = description
            )
    }

    fun toDomain(fromUser: User, toUser: User, currency: Currency) =
        Debt(
            from = fromUser,
            to = toUser,
            currency = currency,
            sum = sum,
            creationDate = creationDate,
            expirationDate = expirationDate,
            description = description,
        )
}
