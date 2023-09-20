package com.sergstas.debtstracker.domain.usecases.auth

import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import javax.inject.Inject

class GetAuthedUserUseCase @Inject constructor(
    private val authRepo: IAuthRepo,
) {
    suspend operator fun invoke(): User? =
        authRepo.getAuthedUser()
}