package com.pawsaver.app.feature.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewPasswordBody(
    @SerialName("new_password")
    val newPassword: String
)

@Serializable
data class NewPasswordResponse(
    val message: String,
)
