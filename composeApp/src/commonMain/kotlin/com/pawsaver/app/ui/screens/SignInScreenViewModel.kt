package com.pawsaver.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.data.SignInBody
import com.pawsaver.app.network.PawsaverApi
import io.ktor.client.call.body
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
        data class Error(val error: String) : SignInScreenState()
        data class Success(val user: SignInBody) : SignInScreenState()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.update { SignInScreenState.Loading }
            try {
                val response = pawsaverApi.login(
                    email, password
                )
                _signInState.update {
                    SignInScreenState.Success(response.body())
                }
            } catch (e: Exception) {
                Logger.e(e.message.toString(), e)
                _signInState.update {
                    SignInScreenState.Error(e.toString())
                }
            }
        }
    }

    fun resetState() {
        _signInState.update {
            SignInScreenState.Idle
        }
    }
}