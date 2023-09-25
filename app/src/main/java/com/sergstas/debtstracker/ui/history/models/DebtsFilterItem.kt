package com.sergstas.debtstracker.ui.history.models

import com.sergstas.debtstracker.util.views.rv.BaseItem
import org.apache.commons.lang3.StringUtils

data class DebtsFilterItem(
    val code: String,
    val isSelected: Boolean,
    val onClick: (DebtsFilterItem) -> Unit,
): BaseItem {
    val displayedName: String
        get() = StringUtils.capitalize(StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(code), StringUtils.SPACE))
}
