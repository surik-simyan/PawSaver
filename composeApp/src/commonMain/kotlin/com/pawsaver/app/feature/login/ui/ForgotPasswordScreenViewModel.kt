package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.login.data.ForgotPasswordResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ForgotPasswordScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _forgotPasswordState: MutableStateFlow<ForgotPasswordScreenState> =
        MutableStateFlow(ForgotPasswordScreenState.Idle)
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    sealed class ForgotPasswordScreenState {
        data object Idle : ForgotPasswordScreenState()
        data object Loading : ForgotPasswordScreenState()
        data class Error(val error: ApiData.Error) : ForgotPasswordScreenState()
        data class Success(val response: ForgotPasswordResponse) : ForgotPasswordScreenState()
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _forgotPasswordState.update { ForgotPasswordScreenState.Loading }
            pawsaverApi.forgotPassword(email)
                .onSuccess { response ->
                    Logger.d("Shelter Sign-up successful: ${response.data}")
                    _forgotPasswordState.update { ForgotPasswordScreenState.Success(response.data) }
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
                    Logger.e("Shelter Sign-up failed: $error", e)
                    _forgotPasswordState.update { ForgotPasswordScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _forgotPasswordState.update {
            ForgotPasswordScreenState.Idle
        }
    }
}