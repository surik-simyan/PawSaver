package com.pawsaver.app.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.login.data.ShelterSignUpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShelterSignUpScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _shelterSignUpState: MutableStateFlow<ShelterSignUpScreenState> =
        MutableStateFlow(ShelterSignUpScreenState.Idle)
    val shelterSignUpState = _shelterSignUpState.asStateFlow()

    sealed class ShelterSignUpScreenState {
        data object Idle : ShelterSignUpScreenState()
        data object Loading : ShelterSignUpScreenState()
        data class Error(val error: ApiData.Error) : ShelterSignUpScreenState()
        data class Success(val user: ShelterSignUpResponse) : ShelterSignUpScreenState()
    }

    fun shelterSignUp(
        email: String,
        name: String,
        registrationNumber: String,
        phone: String,
        password: String
    ) {
        viewModelScope.launch {
            _shelterSignUpState.update { ShelterSignUpScreenState.Loading }
            pawsaverApi.registerShelter(
                email, name, registrationNumber, phone, password
            )
                .onSuccess { response ->
                    Logger.d("Shelter Sign-up successful: ${response.data}")
                    _shelterSignUpState.update { ShelterSignUpScreenState.Success(response.data) }
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
                    _shelterSignUpState.update { ShelterSignUpScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _shelterSignUpState.update {
            ShelterSignUpScreenState.Idle
        }
    }
}