package com.sergstas.debtstracker.ui.friends.models

import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.util.views.rv.BaseItem

data class FriendListItem(
    val origin: User,
    val username: String,
    val friendAvatarUrl: String?,
    val isReal: Boolean,
    val debtsByCurrencies: Map<Currency, Double>,
    val onClick: (FriendListItem) -> Unit,
): BaseItem
