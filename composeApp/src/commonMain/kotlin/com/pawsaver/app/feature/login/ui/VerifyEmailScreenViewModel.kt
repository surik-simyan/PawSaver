package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.core.utils.errorOrNull
import com.pawsaver.app.feature.login.data.VerifyEmailError
import com.pawsaver.app.feature.login.data.VerifyEmailResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VerifyEmailScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _verifyState: MutableStateFlow<VerifyScreenState> =
        MutableStateFlow(VerifyScreenState.Idle)
    val verifyState = _verifyState.asStateFlow()

    sealed class VerifyScreenState {
        data object Idle : VerifyScreenState()
        data object Loading : VerifyScreenState()
        data class Error(val error: VerifyEmailError) : VerifyScreenState()
        data class Success(val response: VerifyEmailResponse) : VerifyScreenState()
    }

    fun verifyEmail(
        email: String,
        code: String
    ) {
        viewModelScope.launch {
            _verifyState.update { VerifyScreenState.Loading }
            pawsaverApi.verify(
                email, code
            )
                .onSuccess { response ->
                    Logger.d("Sign-up successful: ${response.data}")
                    _verifyState.update { VerifyScreenState.Success(response.data) }
                }
                .onFailure { e ->
                    val error = when (e) {
                        is CustomApiException -> e.error as VerifyEmailError
                        else -> VerifyEmailError(_genericErrors = e.errorOrNull())
                    }
                    Logger.e("Sign-up failed: $error", e)
                    _verifyState.update { VerifyScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _verifyState.update {
            VerifyScreenState.Idle
        }
    }
}