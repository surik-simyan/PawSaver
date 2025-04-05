package com.pawsaver.app.feature.login.data

import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.utils.isNotNullOrEmpty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyEmailBody(
    val email: String,
    val code: String
)

@Serializable
data class VerifyEmailResponse(
    val message: String
)

@Serializable
data class VerifyEmailError(
    @SerialName("non_field_errors")
    override val _genericErrors: List<String>? = null
) : ApiData.Error()
