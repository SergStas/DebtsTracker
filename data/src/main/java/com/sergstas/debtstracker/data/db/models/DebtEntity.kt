package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User

@Entity
data class DebtEntity(
    val fromUser: String,
    val toUser: String,
    val currency: String,
    val sum: Double,
    val status: String,
    val creationDate: Long,
    val expirationDate: Long?,
    val description: String?,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    companion object {
        fun Debt.toDbEntity() =
            DebtEntity(
                fromUser = lender.guid,
                toUser = borrower.guid,
                currency = currency.name,
                sum = sum,
                status = status.name,
                creationDate = creationDate,
                expirationDate = expirationDate,
                description = description,
            )
    }

    fun toDomain(fromUser: User, toUser: User) =
        Debt(
            lender = fromUser,
            borrower = toUser,
            currency = Currency.valueOf(currency),
            status = Debt.Status.valueOf(status),
            sum = sum,
            creationDate = creationDate,
            expirationDate = expirationDate,
            description = description,
        )
}
