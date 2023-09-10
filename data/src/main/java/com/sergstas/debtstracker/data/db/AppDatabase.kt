package com.sergstas.debtstracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sergstas.debtstracker.data.db.dao.CurrencyDao
import com.sergstas.debtstracker.data.db.dao.DebtDao
import com.sergstas.debtstracker.data.db.dao.UserDao
import com.sergstas.debtstracker.data.db.models.CurrencyEntity
import com.sergstas.debtstracker.data.db.models.DebtEntity
import com.sergstas.debtstracker.data.db.models.UserEntity

@Database(
    version = 1,
    entities = [
        CurrencyEntity::class,
        UserEntity::class,
        DebtEntity::class,
    ],
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
    abstract fun getUserDao(): UserDao
    abstract fun getDebtDao(): DebtDao
}