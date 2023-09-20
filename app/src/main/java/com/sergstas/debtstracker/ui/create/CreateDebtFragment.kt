package com.sergstas.debtstracker.ui.create

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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
import com.sergstas.debtstracker.util.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

@AndroidEntryPoint
class CreateDebtFragment: Fragment(R.layout.fragment_create) {
    private val binding by viewBinding(FragmentCreateBinding::bind)

    private val viewModel by viewModels<CreateDebtViewModel>()

    private lateinit var currencyAdapter: ArrayAdapter<String>
    private lateinit var userAdapter: ArrayAdapter<String>

    private var expirationDate: Long? = null
    private var currency: String? = null
    private var recipient: String? = null
    private var isIncoming = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        setView()
        observeState()
        loadSpinnerContents()
    }

    private fun loadSpinnerContents() {
        viewModel.loadCurrencies()
        viewModel.loadFriendsList()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is CreateDebtViewModel.Event.CurrenciesListLoaded -> submitCurrencyList(it.currencies)
                    is CreateDebtViewModel.Event.DescriptionEnabled -> switchDescriptionFieldVisibility(it.value)
                    is CreateDebtViewModel.Event.ExpirationEnabled -> switchExpirationDateFieldVisibility(it.value)
                    is CreateDebtViewModel.Event.Error -> displayError(it)
                    is CreateDebtViewModel.Event.FriendsListLoaded -> submitUserList(it.users)
                    is CreateDebtViewModel.Event.Loading -> displayLoading(it.value)
                    CreateDebtViewModel.Event.Success -> navigateBack()
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
        binding.spinCurrency.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currency = parent?.getItemAtPosition(position) as? String
            }
        }
        binding.spinClient.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                recipient = parent?.getItemAtPosition(position) as? String
            }
        }
        binding.spinClient.adapter = userAdapter
        binding.spinCurrency.adapter = currencyAdapter
        binding.spinType.run {
            adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.create_debt_types,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            )
            onItemSelectedListener = object: OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    isIncoming = position == 0
                }
            }
        }
    }

    private fun setView() {
        binding.run {
            cardDesc.setOnClickListener {
                cbDesc.isChecked = !cbDesc.isChecked
                viewModel.onCheckDescription(cbDesc.isChecked)
            }
            cardDate.setOnClickListener {
                cbLimit.isChecked = !cbLimit.isChecked
                viewModel.onCheckExpiration(cbLimit.isChecked)
            }
            bDone.setOnClickListener {
                viewModel.validate(
                    sum = etSum.text.toString(),
                    selectedClientUserName = recipient,
                    expirationDate = expirationDate,
                    isIncoming = isIncoming,
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

    private fun displayError(error: CreateDebtViewModel.Event.Error) {
        binding.etSum.error = null
        val message = getString(when (error) {
            CreateDebtViewModel.Event.Error.ClientIsNull -> R.string.create_er_client_null
            CreateDebtViewModel.Event.Error.ExpirationDateInvalid -> R.string.create_er_date_invalid
            CreateDebtViewModel.Event.Error.SumIsEmpty -> R.string.create_er_sum_empty
            CreateDebtViewModel.Event.Error.SumIsNegative -> R.string.create_er_sum_negative
            CreateDebtViewModel.Event.Error.SumIsZero -> R.string.create_er_sum_zero
        } )
        if (error is CreateDebtViewModel.Event.Error.ClientIsNull
            || error is CreateDebtViewModel.Event.Error.ExpirationDateInvalid) {
            toast(message)
        } else {
            binding.etSum.error = message
        }
    }

    private fun chooseDate() {
        val newFragment = DatePickerFragment {
            expirationDate = it
            val date = LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC)
            binding.tvDate.text = getString(R.string.create_date_format_ph)
                .format(date.dayOfMonth, date.month.value.inc(), date.year)
            binding.bDone.isEnabled = true
        }
        newFragment.show(childFragmentManager, "datePicker")
    }

    private fun navigateBack() =
        findNavController().popBackStack()
}