package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.domain.models.AuthArgs
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepo @Inject constructor(

): IAuthRepo {
    private val dispatcher = Dispatchers.IO

    override suspend fun login(authArgs: AuthArgs): IAuthRepo.AuthResult.Login =
        withContext(dispatcher) {
            IAuthRepo.AuthResult.Login.Invalid
        }

    override suspend fun register(authArgs: AuthArgs): IAuthRepo.AuthResult.Register =
        withContext(dispatcher) {
            IAuthRepo.AuthResult.Register.UsernameIsTaken
        }

    override suspend fun getAuthedUser(): User? =
        withContext(dispatcher) {
            null
        }
}