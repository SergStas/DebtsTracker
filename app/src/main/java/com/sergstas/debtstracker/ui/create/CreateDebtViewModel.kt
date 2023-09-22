package com.sergstas.debtstracker.ui.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.usecases.currencies.GetCurrenciesListUseCase
import com.sergstas.debtstracker.domain.usecases.debts.CreateDebtUseCase
import com.sergstas.debtstracker.domain.usecases.auth.GetAuthedUserUseCase
import com.sergstas.debtstracker.domain.usecases.users.GetFriendsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class CreateDebtViewModel @Inject constructor(
    private val createDebt: CreateDebtUseCase,
    private val getCurrenciesList: GetCurrenciesListUseCase,
    private val getFriendsList: GetFriendsListUseCase,
    private val getAuthedUser: GetAuthedUserUseCase,
): ViewModel() {
    val state get() = _state.asSharedFlow()
    private val _state = MutableSharedFlow<Event>()

    private var friendsList = listOf<User>()

    fun loadFriendsList() {
        viewModelScope.launch {
            _state.emit(Event.Loading(true))
            friendsList = getFriendsList()
            _state.emit(Event.FriendsListLoaded(friendsList))
            _state.emit(Event.Loading(false))
        }
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _state.emit(Event.Loading(true))
            _state.emit(Event.CurrenciesListLoaded(getCurrenciesList()))
            _state.emit(Event.Loading(false))
        }
    }

    fun onCheckExpiration(value: Boolean) {
        viewModelScope.launch {
            _state.emit(Event.ExpirationEnabled(value))
        }
    }

    fun onCheckDescription(value: Boolean) {
        viewModelScope.launch {
            _state.emit(Event.DescriptionEnabled(value))
        }
    }

    fun validate(
        sum: String,
        selectedClientUserName: String?,
        expirationDate: Long?,
        isIncoming: Boolean,
        currency: String,
        description: String?,
    ) {
        viewModelScope.launch {
            _state.emit(Event.Loading(true))
            val result = when {
                sum.isEmpty() -> Event.Error.SumIsEmpty
                abs(sum.toDouble()) < 1e-5 -> Event.Error.SumIsZero
                sum.toDouble() < 0 -> Event.Error.SumIsNegative
                selectedClientUserName == null -> Event.Error.ClientIsNull
                expirationDate?.let { it < System.currentTimeMillis() } ?: false -> Event.Error.ExpirationDateInvalid
                else -> Event.Success
            }
            if (result is Event.Success) {
                val debt = Debt(
                    owner = getAuthedUser()!!,
                    to = friendsList.first { it.username == selectedClientUserName!! },
                    direction = if (isIncoming) Debt.Direction.INCOMING else Debt.Direction.OUTGOING,
                    currency = currency,
                    sum = sum.toDouble(),
                    creationDate = System.currentTimeMillis(),
                    expirationDate = expirationDate,
                    description = description,
                    status = Debt.Status.ASSIGNED,
                )
                createDebt(debt)
            }
            _state.emit(result)
            _state.emit(Event.Loading(false))
        }
    }

    sealed interface Event {
        data class Loading(val value: Boolean): Event
        data class FriendsListLoaded(val users: List<User>): Event
        data class CurrenciesListLoaded(val currencies: List<String>): Event
        data class ExpirationEnabled(val value: Boolean): Event
        data class DescriptionEnabled(val value: Boolean): Event
        sealed interface Error: Event {
            object SumIsEmpty: Error
            object SumIsNegative: Error
            object SumIsZero: Error
            object ClientIsNull: Error
            object ExpirationDateInvalid: Error
        }
        object Success: Event
    }
}