package com.sergstas.debtstracker.ui.splash

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.sergstas.debtstracker.domain.usecases.auth.GetAuthedUserUseCase
import com.sergstas.debtstracker.ui.abstractions.BaseViewModel
import com.sergstas.debtstracker.util.SPLASH_SCREEN_MIN_DURATION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAuthedUser: GetAuthedUserUseCase,
    application: Application,
): BaseViewModel(application) {
    val isAuthed get() = _isAuthed.asSharedFlow()
    private val _isAuthed = MutableSharedFlow<Boolean>()

    fun dispatch() {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_MIN_DURATION)
            _isAuthed.emit(getAuthedUser() != null)
        }
    }
}