package com.sergstas.debtstracker.domain.usecases.friends

import com.sergstas.debtstracker.domain.repo.IFriendsRepo
import javax.inject.Inject

class GetFriendsListUseCase @Inject constructor(
    private val friendsRepo: IFriendsRepo,
) {
    suspend operator fun invoke() =
        friendsRepo.getAll()
}