package com.sergstas.debtstracker.data.db.callbacks

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object CreateInitialCurrencyListCallback: RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            db.insert("CurrencyEntity", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
                put("id", 1)
                put("code", "RUB")
            } )
            db.insert("CurrencyEntity", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
                put("id", 2)
                put("code", "USD")
            } )
            db.insert("CurrencyEntity", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
                put("id", 3)
                put("code", "EUR")
            } )
        }
    }
}