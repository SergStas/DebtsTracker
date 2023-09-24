package com.sergstas.debtstracker.util.views.rv

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T: BaseItem>(itemView: View): RecyclerView.ViewHolder(itemView) {
    protected val context: Context get() = itemView.context

    abstract fun bind(data: T)
}