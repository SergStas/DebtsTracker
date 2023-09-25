package com.sergstas.debtstracker.ui.history.vh

import android.content.res.ColorStateList
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.ItemHistoryFilterBinding
import com.sergstas.debtstracker.ui.history.models.DebtsFilterItem
import com.sergstas.debtstracker.util.extensions.colorFromId
import com.sergstas.debtstracker.util.views.rv.BaseViewHolder

class DebtsFilterViewHolder(
    private val binding: ItemHistoryFilterBinding,
): BaseViewHolder<DebtsFilterItem>(binding.root) {
    override fun bind(data: DebtsFilterItem) {
        binding.run {
            root.backgroundTintList = ColorStateList.valueOf(context.colorFromId(
                if (data.isSelected) R.color.c1 else R.color.c7,
            ))
            root.setOnClickListener { data.onClick(data) }
            tvContent.text = data.displayedName
        }
    }
}