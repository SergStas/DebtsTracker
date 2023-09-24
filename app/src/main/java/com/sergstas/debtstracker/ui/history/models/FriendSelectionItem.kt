package com.sergstas.debtstracker.ui.history.models

import com.sergstas.debtstracker.util.views.rv.BaseItem

data class FriendSelectionItem(
    val username: String,
    val avatarUrl: String?,
    val isSelected: Boolean,
    val onClick: (FriendSelectionItem) -> Unit,
): BaseItem
