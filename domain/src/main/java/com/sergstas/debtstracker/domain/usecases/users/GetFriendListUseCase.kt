package com.sergstas.debtstracker.domain.usecases.users

import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import com.sergstas.debtstracker.domain.repo.IFriendsRepo
import javax.inject.Inject

class GetFriendListUseCase @Inject constructor(
    private val friendsRepo: IFriendsRepo,
    private val authRepo: IAuthRepo,
) {
    suspend operator fun invoke(): List<User> {
        val authedUser = authRepo.getAuthedUser() ?: return emptyList()
        return friendsRepo.getAll().filter { it.guid != authedUser.guid }
    }
}