package com.sergstas.debtstracker.ui.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentMenuBinding

class MenuFragment: Fragment(R.layout.fragment_menu) {
    private val binding by viewBinding(FragmentMenuBinding::bind)

    private val childNavController get() =
        (childFragmentManager.findFragmentById(R.id.menu_fragment_host) as NavHostFragment).navController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNavHost()
    }

    private fun setNavHost() =
        NavigationUI.setupWithNavController(binding.bnvTabs, childNavController)
}