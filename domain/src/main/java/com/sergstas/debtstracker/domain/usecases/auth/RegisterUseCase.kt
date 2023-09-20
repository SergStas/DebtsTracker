package com.sergstas.debtstracker.domain.usecases.auth

import com.sergstas.debtstracker.domain.models.AuthArgs
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepo: IAuthRepo,
) {
    suspend operator fun invoke(username: String, password: String) =
        authRepo.register(AuthArgs(username, password))
}