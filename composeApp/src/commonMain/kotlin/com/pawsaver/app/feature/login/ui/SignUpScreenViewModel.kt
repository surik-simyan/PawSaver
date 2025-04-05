package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.core.utils.errorOrNull
import com.pawsaver.app.feature.login.data.SignUpError
import com.pawsaver.app.feature.login.data.SignUpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _signUpState: MutableStateFlow<SignUpScreenState> =
        MutableStateFlow(SignUpScreenState.Idle)
    val signUpState = _signUpState.asStateFlow()

    sealed class SignUpScreenState {
        data object Idle : SignUpScreenState()
        data object Loading : SignUpScreenState()
        data class Error(val error: SignUpError) : SignUpScreenState()
        data class Success(val user: SignUpResponse) : SignUpScreenState()
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String
    ) {
        viewModelScope.launch {
            _signUpState.update { SignUpScreenState.Loading }
            pawsaverApi.register(
                firstName, lastName, email, password, phone
            )
                .onSuccess { response ->
                    Logger.d("Sign-up successful: ${response.data}")
                    _signUpState.update { SignUpScreenState.Success(response.data) }
                }
                .onFailure { e ->
                    val error = when (e) {
                        is CustomApiException -> e.error as SignUpError
                        else -> SignUpError(_genericErrors = e.errorOrNull())
                    }
                    Logger.e("Sign-up failed: $error", e)
                    _signUpState.update { SignUpScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _signUpState.update {
            SignUpScreenState.Idle
        }
    }
}