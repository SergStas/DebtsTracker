package com.sergstas.debtstracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergstas.debtstracker.domain.usecases.users.GetDebtorsCountUseCase
import com.sergstas.debtstracker.domain.usecases.users.GetDebtsCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDebtsCount: GetDebtsCountUseCase,
    private val getDebtorsCount: GetDebtorsCountUseCase,
): ViewModel() {
    val state get() = _state.asSharedFlow()
    private val _state = MutableSharedFlow<Event>()

    fun loadData() {
        viewModelScope.launch {
            _state.emit(Event.GotDebtsCount(getDebtsCount()))
            _state.emit(Event.GotDebtorsCount(getDebtorsCount()))
        }
    }

    sealed interface Event {
        data class GotDebtsCount(val count: Int): Event
        data class GotDebtorsCount(val count: Int): Event
    }
}