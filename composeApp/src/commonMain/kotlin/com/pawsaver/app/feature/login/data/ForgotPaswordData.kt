package com.pawsaver.app.feature.login.data

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordBody(
    val email: String
)

@Serializable
data class ForgotPasswordResponse(
    val message: String,
    val identifier: String
)
