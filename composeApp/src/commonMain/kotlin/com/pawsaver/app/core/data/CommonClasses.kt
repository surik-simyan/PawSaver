package com.pawsaver.app.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ApiData<T, E : ApiData.Error> {
    @Serializable
    data class Request<T>(
        val data: T
    ) : ApiData<T, Error>()

    @Serializable
    data class Response<T>(
        val data: T
    ) : ApiData<T, Error>()

    @Serializable
    abstract class Error : ApiData<Nothing, Error>() {
        @SerialName("non_field_errors")
        protected abstract val _genericErrors: List<String>?

        // Public property with default value
        val value: String
            get() = _genericErrors?.firstOrNull() ?: "An unexpected error occurred"
    }
}

class CustomApiException(val error: ApiData.Error) : Exception(error.value)