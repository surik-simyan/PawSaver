package com.pawsaver.app.feature.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.main.data.ListingsResponse
import com.pawsaver.app.feature.main.data.ProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _profileScreenState: MutableStateFlow<ProfileScreenState> =
        MutableStateFlow(ProfileScreenState.Idle)
    val profileScreenState = _profileScreenState.asStateFlow()

    sealed class ProfileScreenState {
        data object Idle : ProfileScreenState()
        data object Loading : ProfileScreenState()
        data class Error(val error: ApiData.Error) : ProfileScreenState()
        data class Success(val response: ProfileResponse) : ProfileScreenState()
    }

    init {
        getProfile()
    }

    private fun getProfile() {
        viewModelScope.launch {
            _profileScreenState.update { ProfileScreenState.Loading }
            pawsaverApi.profile()
                .onSuccess { response ->
                    Logger.d("Profile get successful: ${response.data}")
                    _profileScreenState.update { ProfileScreenState.Success(response.data) }
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
                    Logger.e("Listing get failed: $error", e)
                    _profileScreenState.update { ProfileScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _profileScreenState.update {
            ProfileScreenState.Idle
        }
    }
}