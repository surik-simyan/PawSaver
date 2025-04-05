package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.core.utils.errorOrNull
import com.pawsaver.app.feature.login.data.SignInError
import com.pawsaver.app.feature.login.data.SignInResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _signInState: MutableStateFlow<SignInScreenState> =
        MutableStateFlow(SignInScreenState.Idle)
    val signInState = _signInState.asStateFlow()

    sealed class SignInScreenState {
        data object Idle : SignInScreenState()
        data object Loading : SignInScreenState()
        data class Error(val error: SignInError) : SignInScreenState()
        data class Success(val user: SignInResponse) : SignInScreenState()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.update { SignInScreenState.Loading }
            pawsaverApi.login(email, password)
                .onSuccess { response ->
                    Logger.d("Sign-in successful: ${response.data}")
                    _signInState.update { SignInScreenState.Success(response.data) }
                }
                .onFailure { e ->
                    val error = when (e) {
                        is CustomApiException -> e.error as SignInError
                        else -> SignInError(_genericErrors = e.errorOrNull())
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