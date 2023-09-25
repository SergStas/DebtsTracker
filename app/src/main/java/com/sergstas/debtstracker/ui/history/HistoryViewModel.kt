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
import com.sergstas.debtstracker.util.extensions.replaceFirst
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

    private val friendItems = mutableListOf<FriendSelectionItem>()
    private val filterItems = mutableListOf<DebtsFilterItem>()

    fun loadContents() {
        viewModelScope.launch {
            _state.emit(State.Loading(true))
            user = getAuthedUser()
            friendItems.addAll(getFriendsList().map(::mapFriend))
            filterItems.addAll(getFilters())
            _state.emit(State.DebtList.Emitted(getAllDebts(user!!).map(::mapDebt)))
            _state.emit(State.FriendList.Emitted(friendItems))
            _state.emit(State.FilterList.Emitted(filterItems))
            _state.emit(State.Loading(false))
        }
    }

    private fun mapDebt(debt: Debt) =
        DebtHistoryItem(
            itemId = idGenerator(),
            isIncoming = debt.lender == user,
            userAvatarUrl = null,
            userNameToken = if (debt.lender == user) debt.borrower.username else debt.lender.username,
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
            origin = user,
            username = user.username,
            avatarUrl = null,
            isSelected = false,
            onClick = ::onClickFriend,
        )

    private fun getFilters() =
        GetAllDebtsUseCase.FilterArgs.DebtTag.values().map {
            DebtsFilterItem(
                code = it.name,
                isSelected = false,
                onClick = ::onClickFilter,
            )
        }

    private fun onClickFilter(filter: DebtsFilterItem) {
        viewModelScope.launch {
            val updated = filter.copy(isSelected = !filter.isSelected)
            val index = filterItems.replaceFirst(filter, updated)
            _state.emit(State.FilterList.UpdatedAt(index))
            onFilterArgsUpdated()
        }
    }

    private fun onClickFriend(friend: FriendSelectionItem) {
        viewModelScope.launch {
            val updated = friend.copy(isSelected = !friend.isSelected)
            val index = friendItems.replaceFirst(friend, updated)
            _state.emit(State.FriendList.UpdatedAt(index))
            onFilterArgsUpdated()
        }
    }

    private fun onFilterArgsUpdated() {
        viewModelScope.launch {
            _state.emit(State.Loading(true))
            val users = friendItems.filter { it.isSelected }.map(FriendSelectionItem::origin)
            val filters = filterItems.filter { it.isSelected }.map { GetAllDebtsUseCase.FilterArgs.DebtTag.valueOf(it.code) }
            val args = GetAllDebtsUseCase.FilterArgs(users, filters)
            val updatedList = getAllDebts(user!!, args)
            _state.emit(State.DebtList.Emitted(updatedList.map(::mapDebt)))
            _state.emit(State.Loading(false))
        }
    }

    sealed interface State {
        sealed interface FriendList: State {
            data class Emitted(val content: List<FriendSelectionItem>): FriendList
            data class UpdatedAt(val index: Int): FriendList
        }
        sealed interface FilterList: State {
            data class Emitted(val content: List<DebtsFilterItem>): FilterList
            data class UpdatedAt(val index: Int): FilterList
        }
        sealed interface DebtList: State {
            data class Emitted(val content: List<DebtHistoryItem>): DebtList
        }
        data class Loading(val value: Boolean): State
    }
}