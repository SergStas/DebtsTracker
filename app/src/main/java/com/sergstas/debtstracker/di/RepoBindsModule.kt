package com.sergstas.debtstracker.di

import com.sergstas.debtstracker.data.repo.AuthRepo
import com.sergstas.debtstracker.data.repo.DebtRepo
import com.sergstas.debtstracker.data.repo.UserRepo
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import com.sergstas.debtstracker.domain.repo.IDebtRepo
import com.sergstas.debtstracker.domain.repo.IUserRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepoBindsModule {
    @Binds
    fun bindAuthRepo(authRepo: AuthRepo): IAuthRepo

    @Binds
    fun bindDebtRepo(debtRepo: DebtRepo): IDebtRepo

    @Binds
    fun bindUserRepo(userRepo: UserRepo): IUserRepo
}