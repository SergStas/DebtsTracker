package com.sergstas.debtstracker.data.repo

import com.sergstas.debtstracker.data.storage.AppStorage
import com.sergstas.debtstracker.domain.models.AuthArgs
import com.sergstas.debtstracker.domain.models.AuthTokens
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val appStorage: AppStorage,
): IAuthRepo {
    private val dispatcher = Dispatchers.IO

    override suspend fun login(authArgs: AuthArgs): IAuthRepo.AuthResult.Login =
        withContext(dispatcher) {
            delay(500)
            appStorage.storeAuthedUser(User("ABOBA", null, null))
            IAuthRepo.AuthResult.Login.Success(AuthTokens("", ""))
        }

    override suspend fun register(authArgs: AuthArgs): IAuthRepo.AuthResult.Register =
        withContext(dispatcher) {
            delay(500)
            appStorage.storeAuthedUser(User("ABOBA", null, null))
            IAuthRepo.AuthResult.Register.Success(AuthTokens("", ""))
        }

    override suspend fun getAuthedUser(): User? =
        withContext(dispatcher) {
            appStorage.getAuthedUser()
        }
}