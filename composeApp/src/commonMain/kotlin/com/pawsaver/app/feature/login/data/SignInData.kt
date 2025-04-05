package com.pawsaver.app.feature.login.data

import com.pawsaver.app.core.data.ApiData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInBody(
    val email: String,
    val password: String
)

@Serializable
data class SignInResponse(
    val refresh: String,
    val access: String
)

@Serializable
data class SignInError(
    val email: List<String>? = null,
    val password: List<String>? = null,
    @SerialName("non_field_errors")
    override val _genericErrors: List<String>? = null
) : ApiData.Error()
