package com.sergstas.debtstracker.ui.create

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.databinding.FragmentCreateBinding
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.ui.dialogs.DatePickerFragment
import com.sergstas.debtstracker.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateDebtFragment: Fragment(R.layout.fragment_create) {
    private val binding by viewBinding(FragmentCreateBinding::bind)

    private val viewModel by viewModels<CreateDebtViewModel>()

    private lateinit var currencyAdapter: ArrayAdapter<String>
    private lateinit var userAdapter: ArrayAdapter<String>

    private var expirationDate: Long? = null
    private var currency: String? = null
    private var recipient: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        setView()
        collectVmFlow()
        loadSpinnerContents()
    }

    private fun loadSpinnerContents() {
        viewModel.loadCurrencies()
        viewModel.loadFriendsList()
    }

    private fun collectVmFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is CreateDebtViewModel.State.CurrenciesListLoaded -> submitCurrencyList(it.currencies)
                    is CreateDebtViewModel.State.DescriptionEnabled -> switchDescriptionFieldVisibility(it.value)
                    is CreateDebtViewModel.State.ExpirationEnabled -> switchExpirationDateFieldVisibility(it.value)
                    is CreateDebtViewModel.State.Error -> displayError(it)
                    is CreateDebtViewModel.State.FriendsListLoaded -> submitUserList(it.users)
                    is CreateDebtViewModel.State.Loading -> displayLoading(it.value)
                    CreateDebtViewModel.State.Success -> navigateBack()
                }
            }
        }
    }

    private fun submitCurrencyList(currencies: List<String>) {
        currencyAdapter.clear()
        currencyAdapter.addAll(currencies.map { it.uppercase() } )
    }

    private fun submitUserList(usernames: List<User>) {
        userAdapter.clear()
        userAdapter.addAll(usernames.map { it.username } )
    }

    private fun displayLoading(value: Boolean) {
        binding.pbLoading.isVisible = value
    }

    private fun setAdapters() {
        currencyAdapter = ArrayAdapter<String>(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
        )
        userAdapter = ArrayAdapter<String>(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
        )
        binding.spinCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currency = parent?.getItemAtPosition(position) as? String
            }
        }
        binding.spinClient.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                recipient = parent?.getItemAtPosition(position) as? String
            }
        }
        binding.spinClient.adapter = userAdapter
        binding.spinCurrency.adapter = currencyAdapter
    }

    private fun setView() {
        binding.run {
            cbDesc.setOnClickListener {
                viewModel.onCheckDescription(cbDesc.isChecked)
            }
            cbLimit.setOnClickListener {
                viewModel.onCheckExpiration(cbLimit.isChecked)
            }
            bDone.setOnClickListener {
                viewModel.validate(
                    sum = etSum.text.toString(),
                    selectedClientUserName = recipient,
                    expirationDate = expirationDate,
                    isIncoming = true,
                    currency = currency ?: return@setOnClickListener,
                    description = etDesc.text.toString().takeIf { it.isNotEmpty() },
                )
            }
            bSelectDate.setOnClickListener {
                chooseDate()
            }
        }
    }

    private fun switchExpirationDateFieldVisibility(value: Boolean) {
        binding.run {
            bSelectDate.isVisible = value
            tvDate.isVisible = value
            bDone.isEnabled = !value || expirationDate != null
        }
    }

    private fun switchDescriptionFieldVisibility(value: Boolean) {
        binding.etDesc.isVisible = value
    }

    private fun displayError(error: CreateDebtViewModel.State.Error) {
        binding.etSum.error = null
        val message = getString(when (error) {
            CreateDebtViewModel.State.Error.ClientIsNull -> R.string.create_er_client_null
            CreateDebtViewModel.State.Error.ExpirationDateInvalid -> R.string.create_er_date_invalid
            CreateDebtViewModel.State.Error.SumIsEmpty -> R.string.create_er_sum_empty
            CreateDebtViewModel.State.Error.SumIsNegative -> R.string.create_er_sum_negative
            CreateDebtViewModel.State.Error.SumIsZero -> R.string.create_er_sum_zero
        } )
        if (error is CreateDebtViewModel.State.Error.ClientIsNull
            || error is CreateDebtViewModel.State.Error.ExpirationDateInvalid) {
            toast(message)
        } else {
            binding.etSum.error = message
        }
    }

    private fun chooseDate() {
        val newFragment = DatePickerFragment {
            expirationDate = it
            binding.tvDate.text = it.toString()
            binding.bDone.isEnabled = true
        }
        newFragment.show(childFragmentManager, "datePicker")
    }

    private fun navigateBack() =
        findNavController().popBackStack()
}