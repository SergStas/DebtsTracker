package com.sergstas.debtstracker.domain.models

data class Debt(
    val owner: User,
    val to: User,
    val direction: Direction,
    val currency: String,
    val sum: Double,
    val creationDate: Long,
    val expirationDate: Long?,
    val description: String?,
    val status: Status,
) {
    enum class Status {
        ASSIGNED, DECLINED, ACCEPTED, PAID, CONFIRMED, CANCELLING, CANCELLED;
    }

    enum class Direction {
        INCOMING, OUTGOING;
    }
}
