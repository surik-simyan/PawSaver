package com.pawsaver.app.data

import kotlinx.serialization.Serializable

@Serializable
data class VerifyBody(
    val email: String,
    val code: String
)