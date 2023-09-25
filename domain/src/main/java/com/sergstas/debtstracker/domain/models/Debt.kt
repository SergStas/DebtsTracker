package com.sergstas.debtstracker.domain.models

data class Debt(
    val lender: User,
    val borrower: User,
    val currency: Currency,
    val sum: Double,
    val creationDate: Long,
    val expirationDate: Long?,
    val description: String?,
    val status: Status,
) {
    enum class Status {
        ASSIGNED, DECLINED, ACCEPTED, PAID, CONFIRMED, CANCELLING, CANCELLED;
    }
}
