package com.sergstas.debtstracker.ui.history.models

import com.sergstas.debtstracker.util.views.rv.BaseItem

data class DebtsFilterItem(
    val title: String,
    val isSelected: Boolean,
    val onClick: (DebtsFilterItem) -> Unit,
): BaseItem
