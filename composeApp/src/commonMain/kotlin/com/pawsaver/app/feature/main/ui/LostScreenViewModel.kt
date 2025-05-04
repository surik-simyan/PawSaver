package com.pawsaver.app.feature.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.core.network.PawsaverApi
import com.pawsaver.app.feature.main.data.ListingsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LostScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _lostScreenState: MutableStateFlow<LostScreenState> =
        MutableStateFlow(LostScreenState.Idle)
    val lostScreenState = _lostScreenState.asStateFlow()

    sealed class LostScreenState {
        data object Idle : LostScreenState()
        data object Loading : LostScreenState()
        data class Error(val error: ApiData.Error) : LostScreenState()
        data class Success(val response: ListingsResponse) : LostScreenState()
    }

    init {
        getLostAnimals()
    }

    fun getLostAnimals(page: Int = 1) {
        viewModelScope.launch {
            _lostScreenState.update { LostScreenState.Loading }
            pawsaverApi.listings(page)
                .onSuccess { response ->
                    Logger.d("Listing get successful: ${response.data}")
                    _lostScreenState.update { LostScreenState.Success(response.data) }
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
                    _lostScreenState.update { LostScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _lostScreenState.update {
            LostScreenState.Idle
        }
    }
}