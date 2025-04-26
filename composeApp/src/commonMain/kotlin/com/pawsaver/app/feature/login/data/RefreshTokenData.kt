package com.pawsaver.app.feature.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshBody(
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Serializable
data class TokenRefreshResponse(
    val refresh: String,
    val access: String
)
