package com.sergstas.debtstracker.ui.history

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
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
                    is HistoryViewModel.State.DebtList.Emitted -> setDebts(it.content)
                    is HistoryViewModel.State.FilterList.Emitted -> setFilters(it.content)
                    is HistoryViewModel.State.FriendList.Emitted -> setFriends(it.content)
                    is HistoryViewModel.State.FilterList.UpdatedAt -> updateFilter(it.index)
                    is HistoryViewModel.State.FriendList.UpdatedAt -> updateFriend(it.index)
                    is HistoryViewModel.State.Loading -> displayLoading(it.value)
                }
            }
        }
    }

    private fun setAdapters() {
        debtsAdapter = BaseListAdapter.create {
            DebtsHistoryViewHolder(ItemDebtBinding.inflate(layoutInflater, it, false))
        }.apply {
            bindToRv(binding.rvDebts)
            binding.rvDebts.itemAnimator = null
        }
        friendsAdapter = BaseListAdapter.create {
            FriendSelectionViewHolder(ItemUserPreviewBinding.inflate(layoutInflater, it, false))
        }.apply {
            bindToRv(
                binding.rvUsers,
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            )
        }
        filtersAdapter = BaseListAdapter.create {
            DebtsFilterViewHolder(ItemHistoryFilterBinding.inflate(layoutInflater, it, false))
        }.apply {
            bindToRv(
                recyclerView = binding.rvFilters,
                layoutManager = FlexboxLayoutManager(requireContext()).apply {
                    justifyContent = JustifyContent.FLEX_START
                    flexDirection = FlexDirection.ROW
                    alignItems = AlignItems.FLEX_START
                    flexWrap = FlexWrap.WRAP
                }
            )
        }
    }

    private fun setFriends(list: List<FriendSelectionItem>) {
        binding.rvUsers.isVisible = list.isNotEmpty()
        friendsAdapter.submitList(list)
    }

    private fun setDebts(list: List<DebtHistoryItem>) {
        debtsAdapter.submitList(null)
        debtsAdapter.submitList(list)
    }

    private fun updateFilter(index: Int) =
        filtersAdapter.notifyItemChanged(index)

    private fun updateFriend(index: Int) =
        friendsAdapter.notifyItemChanged(index)

    private fun setFilters(list: List<DebtsFilterItem>) =
        filtersAdapter.submitList(list)

    private fun displayLoading(value: Boolean) {
        binding.pbDebts.isVisible = value
        binding.rvDebts.isVisible = !value
    }

    private fun loadData() =
        viewModel.loadContents()
}