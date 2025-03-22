package com.pawsaver.app.data

import kotlinx.serialization.Serializable

@Serializable
data class SignInBody(
    val email: String,
    val password: String
)