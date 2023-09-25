package com.sergstas.debtstracker.ui.friends

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sergstas.debtstracker.domain.models.Currency
import com.sergstas.debtstracker.domain.models.User
import com.sergstas.debtstracker.domain.usecases.debts.GetAllDebtsUseCase
import com.sergstas.debtstracker.domain.usecases.debts.GetDebtSumUseCase
import com.sergstas.debtstracker.domain.usecases.users.GetFriendListUseCase
import com.sergstas.debtstracker.ui.abstractions.BaseViewModel
import com.sergstas.debtstracker.ui.friends.models.FriendListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val getFriendList: GetFriendListUseCase,
    private val getDebtSum: GetDebtSumUseCase,
    application: Application,
): BaseViewModel(application) {
    val friends: LiveData<List<FriendListItem>> get() = _friends
    private val _friends = MutableLiveData<List<FriendListItem>>(emptyList())

    fun loadList() {
        viewModelScope.launch {
            _friends.value = getFriendList().map { toItem(it) }
        }
    }

    private suspend fun toItem(user: User) =
        FriendListItem(
            origin = user,
            username = user.username,
            friendAvatarUrl = "",
            isReal = user.isReal,
            debtsByCurrencies = Currency.values().associateWith {
                getDebtSum(GetAllDebtsUseCase.FilterArgs(currencies = listOf(it)))
            },
            onClick = {},
        )
}