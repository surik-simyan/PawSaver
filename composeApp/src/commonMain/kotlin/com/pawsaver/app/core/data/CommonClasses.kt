package com.pawsaver.app.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ApiData<T> {
    @Serializable
    data class Request<T>(
        val data: T
    ) : ApiData<T>()

    @Serializable
    data class Response<T>(
        val data: T
    ) : ApiData<T>()

    @Serializable
    data class Error(
        @SerialName("errors")
        private val _apiErrors: List<ApiError>? = null
    ) : ApiData<Nothing>() {
        @Serializable
        data class ApiError(
            val identifier: String,
            val message: String
        )

        val apiErrors: List<ApiError>
            get() = _apiErrors ?: listOf(
                ApiError(
                    identifier = "unknown",
                    message = "An unexpected error occurred"
                )
            )
    }
}

class CustomApiException(val error: ApiData.Error) : Exception(error.apiErrors.first().message)