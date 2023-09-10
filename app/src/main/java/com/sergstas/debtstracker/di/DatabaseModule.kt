package com.sergstas.debtstracker.di

import android.content.Context
import androidx.room.Room
import com.sergstas.debtstracker.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    companion object {
        private const val APP_DB_NAME = "debtsTrackerDb"
    }

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DB_NAME)
            .build()

    @Provides
    fun provideDebtDao(database: AppDatabase) = database.getDebtDao()

    @Provides
    fun provideUserDao(database: AppDatabase) = database.getUserDao()

    @Provides
    fun provideCurrencyDao(database: AppDatabase) = database.getCurrencyDao()
}