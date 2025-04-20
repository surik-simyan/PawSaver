package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.login.data.ResetPasswordResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _resetPasswordState: MutableStateFlow<ResetPasswordState> =
        MutableStateFlow(ResetPasswordState.Idle)
    val resetPasswordState = _resetPasswordState.asStateFlow()

    sealed class ResetPasswordState {
        data object Idle : ResetPasswordState()
        data object Loading : ResetPasswordState()
        data class Error(val error: ApiData.Error) : ResetPasswordState()
        data class Success(val response: ResetPasswordResponse) : ResetPasswordState()
    }

    fun resetPassword(
        resetCode: String,
        encodedPk: String,
    ) {
        viewModelScope.launch {
            _resetPasswordState.update { ResetPasswordState.Loading }
            pawsaverApi.resetPassword(
                resetCode, encodedPk
            )
                .onSuccess { response ->
                    Logger.d("Verified email successfully: ${response.data}")
                    _resetPasswordState.update { ResetPasswordState.Success(response.data) }
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
                    _resetPasswordState.update { ResetPasswordState.Error(error) }
                }
        }
    }

    fun resetState() {
        _resetPasswordState.update {
            ResetPasswordState.Idle
        }
    }
}