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

class HomeScreenViewModel(
    private val pawsaverApi: PawsaverApi
) : ViewModel() {

    private val _homeScreenState: MutableStateFlow<HomeScreenState> =
        MutableStateFlow(HomeScreenState.Idle)
    val homeScreenState = _homeScreenState.asStateFlow()

    sealed class HomeScreenState {
        data object Idle : HomeScreenState()
        data object Loading : HomeScreenState()
        data class Error(val error: ApiData.Error) : HomeScreenState()
        data class Success(val response: ListingsResponse) : HomeScreenState()
    }

    init {
        getAdoptionAnimals()
    }

    fun getAdoptionAnimals(page: Int = 1) {
        viewModelScope.launch {
            _homeScreenState.update { HomeScreenState.Loading }
            pawsaverApi.listings(page)
                .onSuccess { response ->
                    Logger.d("Listing get successful: ${response.data}")
                    _homeScreenState.update { HomeScreenState.Success(response.data) }
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
                    _homeScreenState.update { HomeScreenState.Error(error) }
                }
        }
    }

    fun resetState() {
        _homeScreenState.update {
            HomeScreenState.Idle
        }
    }
}