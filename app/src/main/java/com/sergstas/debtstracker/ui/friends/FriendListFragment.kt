package com.sergstas.debtstracker.ui.friends

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentFriendsBinding
import com.sergstas.debtstracker.databinding.ItemFriendListBinding
import com.sergstas.debtstracker.ui.friends.models.FriendListItem
import com.sergstas.debtstracker.ui.friends.vh.FriendListViewHolder
import com.sergstas.debtstracker.util.views.rv.BaseListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendListFragment: Fragment(R.layout.fragment_friends) {
    private val binding by viewBinding(FragmentFriendsBinding::bind)
    private val viewModel by viewModels<FriendListViewModel>()
    private lateinit var adapter: BaseListAdapter<FriendListItem, FriendListViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeState()
        loadData()
    }

    private fun observeState() {
        viewModel.friends.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setAdapter() {
        adapter = BaseListAdapter.create {
            FriendListViewHolder(ItemFriendListBinding.inflate(layoutInflater, it, false))
        }.apply { bindToRv(binding.rvFriends) }
    }

    private fun loadData() =
        viewModel.loadList()
}