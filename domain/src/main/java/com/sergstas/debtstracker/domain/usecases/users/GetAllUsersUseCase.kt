package com.sergstas.debtstracker.domain.usecases.users

import com.sergstas.debtstracker.domain.repo.IUserRepo
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val usersRepo: IUserRepo,
) {
    suspend operator fun invoke() =
        usersRepo.getAll()
}