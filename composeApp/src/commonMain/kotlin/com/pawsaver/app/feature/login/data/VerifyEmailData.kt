package com.pawsaver.app.feature.login.data

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