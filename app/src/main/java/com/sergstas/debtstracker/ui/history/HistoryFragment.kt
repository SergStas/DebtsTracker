package com.sergstas.debtstracker.ui.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentHistoryBinding
import com.sergstas.debtstracker.databinding.ItemDebtBinding
import com.sergstas.debtstracker.databinding.ItemHistoryFilterBinding
import com.sergstas.debtstracker.databinding.ItemUserPreviewBinding
import com.sergstas.debtstracker.ui.history.models.DebtHistoryItem
import com.sergstas.debtstracker.ui.history.models.DebtsFilterItem
import com.sergstas.debtstracker.ui.history.models.FriendSelectionItem
import com.sergstas.debtstracker.ui.history.vh.DebtsFilterViewHolder
import com.sergstas.debtstracker.ui.history.vh.DebtsHistoryViewHolder
import com.sergstas.debtstracker.ui.history.vh.FriendSelectionViewHolder
import com.sergstas.debtstracker.util.views.rv.BaseListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment: Fragment(R.layout.fragment_history) {
    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val viewModel by viewModels<HistoryViewModel>()

    private lateinit var friendsAdapter: BaseListAdapter<FriendSelectionItem, FriendSelectionViewHolder>
    private lateinit var filtersAdapter: BaseListAdapter<DebtsFilterItem, DebtsFilterViewHolder>
    private lateinit var debtsAdapter: BaseListAdapter<DebtHistoryItem, DebtsHistoryViewHolder>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observeState()
        loadData()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when(it) {
                    is HistoryViewModel.State.DebtList -> setDebts(it)
                    is HistoryViewModel.State.FilterList -> setFilters(it)
                    is HistoryViewModel.State.FriendList -> setFriends(it)
                }
            }
        }
    }

    private fun setAdapters() {
        friendsAdapter = BaseListAdapter.create {
            FriendSelectionViewHolder(ItemUserPreviewBinding.inflate(layoutInflater, it, false))
        }.apply { bindToRv(binding.rvUsers) }
        filtersAdapter = BaseListAdapter.create {
            DebtsFilterViewHolder(ItemHistoryFilterBinding.inflate(layoutInflater, it, false))
        }.apply { bindToRv(binding.rvFilters) }
        debtsAdapter = BaseListAdapter.create {
            DebtsHistoryViewHolder(ItemDebtBinding.inflate(layoutInflater, it, false))
        }.apply { bindToRv(binding.rvDebts) }
    }

    private fun setFriends(state: HistoryViewModel.State.FriendList) {
        binding.rvUsers.isVisible = state.friends.isNotEmpty()
        friendsAdapter.submitList(state.friends)
    }

    private fun setFilters(state: HistoryViewModel.State.FilterList) =
        filtersAdapter.submitList(state.filters)

    private fun setDebts(state: HistoryViewModel.State.DebtList) =
        debtsAdapter.submitList(state.debts)

    private fun loadData() =
        viewModel.loadContents()
}