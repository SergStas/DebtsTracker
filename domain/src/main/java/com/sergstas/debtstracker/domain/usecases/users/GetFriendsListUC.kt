package com.sergstas.debtstracker.domain.usecases.users

import com.sergstas.debtstracker.domain.models.User
import javax.inject.Inject

class GetFriendsListUC @Inject constructor(

) {
    suspend operator fun invoke() =
        listOf(User("Друг", null,  null))
}