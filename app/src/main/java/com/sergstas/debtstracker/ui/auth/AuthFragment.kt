package com.sergstas.debtstracker.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentAuthBinding
import com.sergstas.debtstracker.util.PASSWORD_MIN_LENGTH
import com.sergstas.debtstracker.util.USERNAME_MIN_LENGTH
import com.sergstas.debtstracker.util.extensions.toast
import com.sergstas.debtstracker.util.views.ClickSpan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment: Fragment(R.layout.fragment_auth) {
    private val binding by viewBinding(FragmentAuthBinding::bind)
    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAuth()
        setView()
        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.mode.collect {
                onModeChanged(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorized.collect {
                if (it) {
                    navigateToMenu()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect {
                displayError(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loading.collect {
                displayLoading(it)
            }
        }
    }

    private fun setView() {
        binding.run {
            bConfirm.setOnClickListener {
                viewModel.validate(
                    username = etUsername.text.toString().takeIf { it.isNotEmpty() },
                    password = etPassword.text.toString().takeIf { it.isNotEmpty() },
                    passwordRepeat = etPasswordRepeat.text.toString().takeIf { it.isNotEmpty() },
                )
            }
        }
    }

    private fun onModeChanged(mode: AuthViewModel.Mode) {
        binding.run {
            cardPasswordRepeat.isVisible = mode == AuthViewModel.Mode.Register
            authTvPasswordRepeat.isVisible = mode == AuthViewModel.Mode.Register
            authTvTitle.text = getString(when(mode) {
                AuthViewModel.Mode.Login -> R.string.auth_tv_title_login
                AuthViewModel.Mode.Register -> R.string.auth_tv_title_register
            })
            bConfirm.text = getString(when(mode) {
                AuthViewModel.Mode.Login -> R.string.auth_b_submit_login
                AuthViewModel.Mode.Register -> R.string.auth_b_submit_register
            })
            tvSwitchMode.text = getString(when(mode) {
                AuthViewModel.Mode.Login -> R.string.auth_tv_to_registration
                AuthViewModel.Mode.Register -> R.string.auth_tv_to_login
            })
            ClickSpan.addActionToText(
                view = tvSwitchMode,
                clickableText = getString(when(mode) {
                    AuthViewModel.Mode.Login -> R.string.auth_tv_to_registration_link
                    AuthViewModel.Mode.Register -> R.string.auth_tv_to_login_link
                } ),
                action = { switchMode() },
                context = requireContext(),
            )
        }
    }

    private fun displayError(error: AuthViewModel.Error?) {
        binding.run {
            etUsername.error = (error as? AuthViewModel.Error.Username)?.let {
                getString(it.msgId).run {
                    if (error == AuthViewModel.Error.Username.TooShort) format(USERNAME_MIN_LENGTH)
                    else this
                }
            }
            etPassword.error = (error as? AuthViewModel.Error.Password)?.let {
                getString(it.msgId).run {
                    if (error == AuthViewModel.Error.Password.TooShort) format(PASSWORD_MIN_LENGTH)
                    else this
                }
            }
            if (error != null && error !is AuthViewModel.Error.Password && error !is AuthViewModel.Error.Username) {
                toast(getString(error.msgId))
            }
        }
    }

    private fun displayLoading(value: Boolean) {
        binding.pbLoading.isVisible = value
    }

    private fun checkAuth() =
        viewModel.checkAuth()

    private fun switchMode() =
        viewModel.switchMode()

    private fun navigateToMenu() =
        findNavController().navigate(R.id.action_authFragment_to_nav_menu)
}