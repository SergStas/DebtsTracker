package com.sergstas.debtstracker.ui.history.vh

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.ItemDebtBinding
import com.sergstas.debtstracker.ui.history.models.DebtHistoryItem
import com.sergstas.debtstracker.util.extensions.colorFromId
import com.sergstas.debtstracker.util.views.rv.BaseViewHolder

class DebtsHistoryViewHolder(
    private val binding: ItemDebtBinding,
): BaseViewHolder<DebtHistoryItem>(binding.root) {
    override fun bind(data: DebtHistoryItem) {
        binding.run {
            root.setOnClickListener { data.onClick(data) }
            tvType.text = context.getString(
                if (data.isIncoming) R.string.item_debt_tv_incoming else R.string.item_debt_tv_outgoing
            )
            tvDateCreated.text = data.creationDateToken
            tvDateUntil.isVisible = data.expirationDateToken != null
            tvDateUntil.text = data.expirationDateToken
            tvOverdue.isVisible = data.isOverdue
            tvDescription.isVisible = data.description != null
            tvDescription.text = data.description
            tvDescriptionLabel.text = context.getString(
                if (data.description != null) R.string.item_debt_tv_desc_label_true else R.string.item_debt_tv_desc_label_false,
            )
            tvSum.text = data.sumToken
            tvSum.setTextColor(context.colorFromId(
                if (data.isIncoming) R.color.c5 else R.color.c6,
            ))
            tvStatus.text = data.status
            ivMarkerWarn.isVisible = data.isExpirationNear
            Glide.with(context)
                .load(data.userAvatarUrl)
                .placeholder(R.drawable.ic_user_avatar_ph)
                .into(ivAvatar)
        }
    }
}