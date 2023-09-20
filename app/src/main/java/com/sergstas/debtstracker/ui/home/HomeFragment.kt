package com.sergstas.debtstracker.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        observeState()
        loadData()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is HomeViewModel.Event.GotDebtsCount -> updateDebtsCount(it.count)
                    is HomeViewModel.Event.GotDebtorsCount -> updateDebtorsCount(it.count)
                }
            }
        }
    }

    private fun updateDebtsCount(count: Int) {
        binding.tvDebts.text = resources.getQuantityString(R.plurals.home_tv_debts, count, 1)
    }

    private fun updateDebtorsCount(count: Int) {
        binding.tvDebtors.text = resources.getQuantityString(R.plurals.home_tv_debtors, count, 1)
    }

    private fun setView() {
        binding.run {
            bCreate.setOnClickListener {
                toCreationPage()
            }
            bHistory.setOnClickListener {
                toHistoryPage()
            }
        }
    }

    private fun loadData() =
        viewModel.loadData()

    private fun toCreationPage() =
        findNavController().navigate(
            resId = R.id.action_homeFragment_to_home_createDebtFragment,
        )

    private fun toHistoryPage() =
        findNavController().navigate(
            resId = R.id.action_homeFragment_to_home_historyFragment,
        )
}