package com.sergstas.debtstracker.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentHomeBinding

class HomeFragment: Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
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

    private fun toCreationPage() =
        findNavController().navigate(
            resId = R.id.action_homeFragment_to_home_createDebtFragment,
        )

    private fun toHistoryPage() =
        findNavController().navigate(
            resId = R.id.action_homeFragment_to_home_historyFragment,
        )
}