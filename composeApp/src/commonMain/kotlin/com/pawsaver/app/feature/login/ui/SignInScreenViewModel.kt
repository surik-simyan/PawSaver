package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.login.data.TokenRefreshResponse
import com.pawsaver.app.feature.login.data.SignInResponse
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInScreenViewModel(
    private val pawsaverApi: PawsaverApi,
    private val settings: Settings
) : ViewModel() {

    private val _signInState: MutableStateFlow<SignInScreenState> =
        MutableStateFlow(SignInScreenState.Idle)
    val signInState = _signInState.asStateFlow()

    sealed class SignInScreenState {
        data object Idle : SignInScreenState()
        data object Loading : SignInScreenState()
        data class Error(val error: ApiData.Error) : SignInScreenState()
        data class Success(val user: SignInResponse) : SignInScreenState()
        data class SuccessToken(val user: TokenRefreshResponse) : SignInScreenState()
    }

    init {
        val refreshToken = settings.getStringOrNull("refreshToken")
        if (refreshToken != null) {
            viewModelScope.launch {
                _signInState.update { SignInScreenState.Loading }
                pawsaverApi.refreshToken(refreshToken)
                    .onSuccess { response ->
                        Logger.d("Sign-in successful: ${response.data}")
                        settings.putString("accessToken", response.data.access)
                        settings.putString("refreshToken", response.data.refresh)
                        _signInState.update { SignInScreenState.SuccessToken(response.data) }
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
                        Logger.e("Sign-in failed: $error", e)
                        _signInState.update { SignInScreenState.Error(error) }
                    }
            }
        }
    }

    fun signIn(email: String, password: String, rememberMe: Boolean) {
        viewModelScope.launch {
            _signInState.update { SignInScreenState.Loading }
            pawsaverApi.login(email, password)
                .onSuccess { response ->
                    Logger.d("Sign-in successful: ${response.data}")
                    if (rememberMe || settings.getStringOrNull("refreshToken") != null) {
                        settings.putString("refreshToken", response.data.refresh)
                        settings.putString("accessToken", response.data.access)
                    }
                    _signInState.update { SignInScreenState.Success(response.data) }
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
                    Logger.e("Sign-in failed: $error", e)
                    _signInState.update { SignInScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _signInState.update {
            SignInScreenState.Idle
        }
    }
}