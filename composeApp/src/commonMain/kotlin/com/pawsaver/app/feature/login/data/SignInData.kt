package com.pawsaver.app.feature.login.data

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
