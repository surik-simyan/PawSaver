package com.pawsaver.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.data.SignInBody
import com.pawsaver.app.network.PawsaverApi
import io.ktor.client.call.body
import kotlinx.coroutines.delay
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
        data class Error(val error: String) : SignUpScreenState()
        data class Success(val user: SignInBody) : SignUpScreenState()
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
            try {
                val response = pawsaverApi.register(
                    firstName, lastName, email, password, phone
                )
//                _signUpState.update {
//                    SignInScreenState.Success(response.body())
//                }
            } catch (e: Exception) {
                Logger.e(e.message.toString(), e)
                _signUpState.update {
                    SignUpScreenState.Error(e.toString())
                }
                delay(5000)
                _signUpState.update {
                    SignUpScreenState.Idle
                }
            }
        }
    }
}