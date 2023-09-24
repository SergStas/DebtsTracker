package com.sergstas.debtstracker.util.views.rv

import androidx.recyclerview.widget.DiffUtil

class DefaultDiffUtilCallback<T: BaseItem>: DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    override fun areContentsTheSame(oldItem: T, newItem: T) = areItemsTheSame(oldItem, newItem)
}