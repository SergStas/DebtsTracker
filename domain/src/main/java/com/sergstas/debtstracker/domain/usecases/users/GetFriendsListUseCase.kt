package com.sergstas.debtstracker.domain.usecases.users

import com.sergstas.debtstracker.domain.models.User
import javax.inject.Inject

class GetFriendsListUseCase @Inject constructor(

) {
    suspend operator fun invoke() =
        listOf(User("Друг", null,  null))
}