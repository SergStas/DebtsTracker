package com.sergstas.debtstracker.ui.history.models

import com.sergstas.debtstracker.util.views.rv.BaseItem

data class DebtHistoryItem(
    val itemId: Long,
    val isIncoming: Boolean,
    val userAvatarUrl: String?,
    val userNameToken: String,
    val sumToken: String,
    val creationDateToken: String,
    val expirationDateToken: String?,
    val description: String?,
    val isExpirationNear: Boolean,
    val isOverdue: Boolean,
    val status: String,
    val onClick: (DebtHistoryItem) -> Unit,
): BaseItem
