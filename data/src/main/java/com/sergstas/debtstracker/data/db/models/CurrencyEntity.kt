package com.sergstas.debtstracker.data.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sergstas.debtstracker.domain.models.Currency

@Entity
data class CurrencyEntity(
    val code: String,
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
) {
    companion object {
        fun Currency.toDbEntity() = CurrencyEntity(code)
    }

    fun toDomainData() = Currency(code)
}
