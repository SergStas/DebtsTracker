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
    private val _state = MutableSharedFlow<State>()

    private var friendsList = listOf<User>()

    fun loadFriendsList() {
        viewModelScope.launch {
            _state.emit(State.Loading(true))
            friendsList = getFriendsList()
            _state.emit(State.FriendsListLoaded(friendsList))
            _state.emit(State.Loading(false))
        }
    }

    fun loadCurrencies() {
        viewModelScope.launch {
            _state.emit(State.Loading(true))
            _state.emit(State.CurrenciesListLoaded(getCurrenciesList()))
            _state.emit(State.Loading(false))
        }
    }

    fun onCheckExpiration(value: Boolean) {
        viewModelScope.launch {
            _state.emit(State.ExpirationEnabled(value))
        }
    }

    fun onCheckDescription(value: Boolean) {
        viewModelScope.launch {
            _state.emit(State.DescriptionEnabled(value))
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
            _state.emit(State.Loading(true))
            val result = when {
                sum.isEmpty() -> State.Error.SumIsEmpty
                abs(sum.toDouble()) < 1e-5 -> State.Error.SumIsZero
                sum.toDouble() < 0 -> State.Error.SumIsNegative
                selectedClientUserName == null -> State.Error.ClientIsNull
                expirationDate?.let { it < System.currentTimeMillis() } ?: false -> State.Error.ExpirationDateInvalid
                else -> State.Success
            }
            if (result is State.Success) {
                val debt = Debt(
                    from = getAuthedUser()!!,
                    to = friendsList.first { it.username == selectedClientUserName!! },
                    direction = if (isIncoming) Debt.Direction.INCOMING else Debt.Direction.OUTGOING,
                    currency = currency,
                    sum = sum.toDouble(),
                    creationDate = System.currentTimeMillis(),
                    expirationDate = expirationDate,
                    description = description,
                )
                createDebt(debt)
            }
            _state.emit(result)
            _state.emit(State.Loading(false))
        }
    }

    sealed interface State {
        data class Loading(val value: Boolean): State
        data class FriendsListLoaded(val users: List<User>): State
        data class CurrenciesListLoaded(val currencies: List<String>): State
        data class ExpirationEnabled(val value: Boolean): State
        data class DescriptionEnabled(val value: Boolean): State
        sealed interface Error: State {
            object SumIsEmpty: Error
            object SumIsNegative: Error
            object SumIsZero: Error
            object ClientIsNull: Error
            object ExpirationDateInvalid: Error
        }
        object Success: State
    }
}