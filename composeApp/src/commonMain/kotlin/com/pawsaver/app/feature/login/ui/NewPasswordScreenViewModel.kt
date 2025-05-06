package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.login.data.NewPasswordResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPasswordScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _newPasswordState: MutableStateFlow<NewPasswordScreenState> =
        MutableStateFlow(NewPasswordScreenState.Idle)
    val newPasswordState = _newPasswordState.asStateFlow()

    sealed class NewPasswordScreenState {
        data object Idle : NewPasswordScreenState()
        data object Loading : NewPasswordScreenState()
        data class Error(val error: ApiData.Error) : NewPasswordScreenState()
        data class Success(val user: NewPasswordResponse) : NewPasswordScreenState()
    }

    fun setNewPassword(
        newPassword: String,
        encodedPk: String,
        resetToken: String
    ) {
        viewModelScope.launch {
            _newPasswordState.update { NewPasswordScreenState.Loading }
            pawsaverApi.setNewPassword(
                newPassword, encodedPk, resetToken
            )
                .onSuccess { response ->
                    Logger.d("Sign-up successful: ${response.data}")
                    _newPasswordState.update { NewPasswordScreenState.Success(response.data) }
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
                    _newPasswordState.update { NewPasswordScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _newPasswordState.update {
            NewPasswordScreenState.Idle
        }
    }
}