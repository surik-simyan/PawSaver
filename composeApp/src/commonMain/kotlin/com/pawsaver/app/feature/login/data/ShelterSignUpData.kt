package com.pawsaver.app.feature.login.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShelterSignUpBody(
    val email: String,
    val name: String,
    @SerialName("registration_number")
    val registrationNumber: String,
    val phone: String,
    val password: String
)

@Serializable
data class ShelterSignUpResponse(
    val message: String,
)
