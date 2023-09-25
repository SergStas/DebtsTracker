package com.sergstas.debtstracker.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sergstas.debtstracker.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment: Fragment(R.layout.fragment_splash) {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
    }

    override fun onResume() {
        super.onResume()
        checkIsAuthed()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAuthed.collect {
                navigateNext(it)
            }
        }
    }

    private fun navigateNext(isAuthed: Boolean) =
        findNavController().navigate(
            resId =
                if (isAuthed) R.id.action_splashFragment_to_nav_main
                else R.id.action_splashFragment_to_authFragment,
        )

    private fun checkIsAuthed() =
        viewModel.dispatch()
}