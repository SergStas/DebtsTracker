package com.sergstas.debtstracker.domain.usecases.users

import com.sergstas.debtstracker.domain.models.User
import javax.inject.Inject

class GetAuthedUserUC @Inject constructor(

) {
    suspend operator fun invoke() =
        User("@B0B@", null, null)
}