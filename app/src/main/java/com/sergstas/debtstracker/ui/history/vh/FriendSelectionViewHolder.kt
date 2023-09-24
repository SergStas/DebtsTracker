package com.sergstas.debtstracker.ui.history.vh

import com.bumptech.glide.Glide
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.ItemUserPreviewBinding
import com.sergstas.debtstracker.ui.history.models.FriendSelectionItem
import com.sergstas.debtstracker.util.views.rv.BaseViewHolder

class FriendSelectionViewHolder(
    private val binding: ItemUserPreviewBinding,
): BaseViewHolder<FriendSelectionItem>(binding.root) {
    override fun bind(data: FriendSelectionItem) {
        binding.run {
            root.setOnClickListener { data.onClick(data) }
            tvName.text = context.getString(R.string.display_username_ph).format(data.username)
            Glide.with(context)
                .load(data.avatarUrl)
                .placeholder(R.drawable.ic_user_avatar_ph)
                .into(ivAvatar)
        }
    }
}