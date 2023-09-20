package com.sergstas.debtstracker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergstas.debtstracker.R
import com.sergstas.debtstracker.domain.repo.IAuthRepo
import com.sergstas.debtstracker.domain.usecases.auth.GetAuthedUserUseCase
import com.sergstas.debtstracker.domain.usecases.auth.LoginUseCase
import com.sergstas.debtstracker.domain.usecases.auth.RegisterUseCase
import com.sergstas.debtstracker.util.USERNAME_MIN_LENGTH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getAuthedUser: GetAuthedUserUseCase,
    private val login: LoginUseCase,
    private val register: RegisterUseCase,
): ViewModel() {
    val mode get() = _mode.asStateFlow()
    private val _mode = MutableStateFlow<Mode>(Mode.Login)

    val authorized get() = _authorized.asStateFlow()
    private val _authorized = MutableStateFlow(false)

    val error get() = _error.asStateFlow()
    private val _error = MutableStateFlow<Error?>(null)

    fun checkAuth() {
        viewModelScope.launch {
            _authorized.value = getAuthedUser() != null
        }
    }

    fun switchMode() {
        viewModelScope.launch {
            _mode.value = if (_mode.value == Mode.Login) Mode.Register else Mode.Login
        }
    }

    fun validate(username: String?, password: String?, passwordRepeat: String?) {
        viewModelScope.launch {
            val validationError = when {
                username == null -> Error.Username.IsEmpty
                password == null -> Error.Password.IsEmpty
                username.length < USERNAME_MIN_LENGTH -> Error.Username.TooShort
                password.length < USERNAME_MIN_LENGTH -> Error.Password.TooShort
                mode.value == Mode.Register && password != passwordRepeat -> Error.Password.NotMatch
                else -> null
            }
            if (validationError == null) {
                val authResult = when(mode.value) {
                    Mode.Login -> login(username!!, password!!)
                    Mode.Register -> register(username!!, password!!)
                }
                val mappedError = when(authResult) {
                    is IAuthRepo.AuthResult.Success -> null
                    IAuthRepo.AuthResult.Login.Invalid -> Error.InvalidCredentials
                    IAuthRepo.AuthResult.Register.PasswordIsInvalid -> Error.Password.TooShort
                    IAuthRepo.AuthResult.Register.UsernameIsInvalid -> Error.Username.TooShort
                    IAuthRepo.AuthResult.Register.UsernameIsTaken -> Error.Username.IsAllocated
                    IAuthRepo.AuthResult.UnknownError -> Error.Unknown
                }
                _error.value = mappedError
                checkAuth()
            } else {
                _error.value = validationError
            }
        }
    }

    sealed interface Mode {
        object Register: Mode
        object Login: Mode
    }

    sealed class Error(val msgId: Int) {
        sealed class Username(msgId: Int): Error(msgId) {
            object IsEmpty: Username(R.string.auth_er_username_is_empty)
            object TooShort: Username(R.string.auth_er_username_too_short)
            object IsAllocated: Username(R.string.auth_er_username_is_allocated)
        }
        sealed class Password(msgId: Int): Error(msgId) {
            object IsEmpty: Password(R.string.auth_er_password_is_empty)
            object TooShort: Password(R.string.auth_er_password_too_short)
            object NotMatch: Password(R.string.auth_er_password_not_match)
        }
        object InvalidCredentials: Error(R.string.auth_er_invalid_credentials)
        object Unknown: Error(R.string.auth_er_unknown)
    }
}