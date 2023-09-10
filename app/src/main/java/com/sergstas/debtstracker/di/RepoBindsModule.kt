package com.sergstas.debtstracker.di

import com.sergstas.debtstracker.data.repo.DebtRepo
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoBindsModule {
    @Binds
    fun bindDebtRepo(debtRepo: DebtRepo): IDebtRepo
}