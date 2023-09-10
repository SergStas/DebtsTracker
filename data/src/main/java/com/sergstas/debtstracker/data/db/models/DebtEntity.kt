package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromUserId: Long,
    val toUserId: Long,
    val currencyId: Long,
    val sum: Double,
    val creationDate: Long,
    val description: String,
) {
    fun toDomain(fromUser: User, toUser: User, currency: Currency) =
        Debt(
            from = fromUser,
            to = toUser,
            currency = currency,
            sum = sum,
            creationDate = creationDate,
            description = description,
        )
}
