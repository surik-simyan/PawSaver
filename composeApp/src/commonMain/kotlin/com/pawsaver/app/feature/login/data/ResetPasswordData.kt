package com.pawsaver.app.feature.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordBody(
    @SerialName("reset_code")
    val resetCode: String
)

@Serializable
data class ResetPasswordResponse(
    val message: String,
    val identifier: String,
    @SerialName("reset_token")
    val resetToken: String
)
