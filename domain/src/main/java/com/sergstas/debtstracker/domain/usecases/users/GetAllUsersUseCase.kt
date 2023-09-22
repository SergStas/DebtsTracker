package com.sergstas.debtstracker.domain.usecases.users

import com.sergstas.debtstracker.domain.repo.IFriendsRepo
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val usersRepo: IFriendsRepo,
) {
    suspend operator fun invoke() =
        usersRepo.getAll()
}