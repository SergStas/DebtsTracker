package com.sergstas.debtstracker.ui.friends.vh

import android.content.res.ColorStateList
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.ItemFriendListBinding
import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.ui.friends.models.FriendListItem
import com.sergstas.debtstracker.util.extensions.colorFromId
import com.sergstas.debtstracker.util.extensions.drawableFromId
import com.sergstas.debtstracker.util.views.rv.BaseViewHolder
import kotlin.math.absoluteValue

class FriendListViewHolder(
    private val binding: ItemFriendListBinding,
): BaseViewHolder<FriendListItem>(binding.root) {
    override fun bind(data: FriendListItem) {
        binding.run {
            root.setOnClickListener { data.onClick(data) }
            ivIsReal.setImageDrawable(context.drawableFromId(
                if (data.isReal) R.drawable.ic_checked else R.drawable.ic_warning,
            ))
            tvImaginary.isVisible = !data.isReal
            tvUsername.text = data.username
            data.debtsByCurrencies.forEach {
                val view = when (it.key) {
                    Currency.Rub -> ivRub
                    Currency.Usd -> ivUsd
                    Currency.Eur -> ivEur
                    Currency.Kzt -> ivKzt
                    Currency.Gel -> ivGel
                }
                view.isVisible = it.value.absoluteValue > 1e-4
                view.imageTintList = ColorStateList.valueOf(context.colorFromId(
                    if (it.value > 0) R.color.c5 else R.color.c6,
                ))
            }
            Glide.with(context)
                .load(data.friendAvatarUrl)
                .placeholder(R.drawable.ic_user_avatar_ph)
                .into(ivAvatar)
        }
    }
}