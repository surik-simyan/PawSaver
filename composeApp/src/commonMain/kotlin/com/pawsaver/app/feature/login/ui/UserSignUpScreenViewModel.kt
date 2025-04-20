package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.login.data.UserSignUpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSignUpScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _userSignUpState: MutableStateFlow<UserSignUpScreenState> =
        MutableStateFlow(UserSignUpScreenState.Idle)
    val userSignUpState = _userSignUpState.asStateFlow()

    sealed class UserSignUpScreenState {
        data object Idle : UserSignUpScreenState()
        data object Loading : UserSignUpScreenState()
        data class Error(val error: ApiData.Error) : UserSignUpScreenState()
        data class Success(val user: UserSignUpResponse) : UserSignUpScreenState()
    }

    fun userSignUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String
    ) {
        viewModelScope.launch {
            _userSignUpState.update { UserSignUpScreenState.Loading }
            pawsaverApi.registerUser(
                firstName, lastName, email, password, phone
            )
                .onSuccess { response ->
                    Logger.d("Sign-up successful: ${response.data}")
                    _userSignUpState.update { UserSignUpScreenState.Success(response.data) }
                }
                .onFailure { e ->
                    val error = when (e) {
                        is CustomApiException -> e.error
                        else -> ApiData.Error(
                            _apiErrors = listOf(
                                ApiData.Error.ApiError(
                                    identifier = "unknown",
                                    message = e.message ?: ""
                                )
                            )
                        )
                    }
                    Logger.e("Sign-up failed: $error", e)
                    _userSignUpState.update { UserSignUpScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _userSignUpState.update {
            UserSignUpScreenState.Idle
        }
    }
}