package com.sergstas.debtstracker.util.views.rv

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseListAdapter<TItem: BaseItem, TVH: BaseViewHolder<TItem>>(
    private val viewHolderFactory: (parent: ViewGroup) -> TVH,
): ListAdapter<TItem, TVH>(DefaultDiffUtilCallback<TItem>()) {
    companion object {
        fun<TItem: BaseItem, TVH: BaseViewHolder<TItem>> create(viewHolderFactory: (parent: ViewGroup) -> TVH) =
            object: BaseListAdapter<TItem, TVH>(viewHolderFactory) {}
    }

    override fun onBindViewHolder(holder: TVH, position: Int) = holder.bind(getItem(position))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = viewHolderFactory(parent)

    fun bindToRv(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(recyclerView.context),
    ) {
        recyclerView.adapter = this
        recyclerView.layoutManager = layoutManager
    }
}