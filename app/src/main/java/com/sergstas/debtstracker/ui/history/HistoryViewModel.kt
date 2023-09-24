package com.sergstas.debtstracker.ui.history

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.domain.models.Debt
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.usecases.auth.GetAuthedUserUseCase
import com.sergstas.debtstracker.domain.usecases.debts.GetAllDebtsUseCase
import com.sergstas.debtstracker.domain.usecases.users.GetFriendsListUseCase
import com.sergstas.debtstracker.ui.abstractions.BaseViewModel
import com.sergstas.debtstracker.ui.history.models.DebtHistoryItem
import com.sergstas.debtstracker.ui.history.models.DebtsFilterItem
import com.sergstas.debtstracker.ui.history.models.FriendSelectionItem
import com.sergstas.debtstracker.util.IdGenerator
import com.sergstas.debtstracker.util.extensions.formatAsDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllDebts: GetAllDebtsUseCase,
    private val getFriendsList: GetFriendsListUseCase,
    private val getAuthedUser: GetAuthedUserUseCase,
    private val idGenerator: IdGenerator,
    application: Application,
): BaseViewModel(application) {
    val state get() = _state.asSharedFlow()
    private val _state = MutableSharedFlow<State>()
    private var user: User? = null

    fun loadContents() {
        viewModelScope.launch {
            user = getAuthedUser()
            _state.emit(State.DebtList(getAllDebts(user ?: return@launch).map(this@HistoryViewModel::mapDebt)))
            _state.emit(State.FriendList(getFriendsList().map(this@HistoryViewModel::mapFriend)))
            _state.emit(State.FilterList(getFilters()))
        }
    }

    private fun mapDebt(debt: Debt) =
        DebtHistoryItem(
            itemId = idGenerator(),
            isIncoming = debt.lender == user,
            userAvatarUrl = null,
            userNameToken = if (debt.lender == user) debt.lender.username else debt.borrower.username,
            sumToken = context.getString(R.string.debt_price_token_ph).format(debt.sum.toString(), debt.currency.lowercase()),
            creationDateToken = debt.creationDate.formatAsDate(context.getString(R.string.app_date_format)),
            expirationDateToken = debt.expirationDate?.run {
                formatAsDate(context.getString(R.string.app_date_format))
            },
            description = debt.description,
            isExpirationNear = System.currentTimeMillis() - debt.creationDate < 1000 * 3600 * 24 * 7L,
            isOverdue = (debt.expirationDate ?: -1) > System.currentTimeMillis(),
            status = debt.status.name,
            onClick = {},
        )


    private fun mapFriend(user: User) =
        FriendSelectionItem(
            username = user.username,
            avatarUrl = null,
            isSelected = false,
            onClick = {},
        )

    private fun getFilters() =
        GetAllDebtsUseCase.FilterArgs.DebtTag.values().map {
            DebtsFilterItem(
                title = it.name,
                isSelected = it == GetAllDebtsUseCase.FilterArgs.DebtTag.All,
                onClick = {},
            )
        }

    sealed interface State {
        data class FilterList(val filters: List<DebtsFilterItem>): State
        data class FriendList(val friends: List<FriendSelectionItem>): State
        data class DebtList(val debts: List<DebtHistoryItem>): State
    }
}