package com.pawsaver.app.feature.login.data

import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.utils.isNotNullOrEmpty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpBody(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String
)

@Serializable
data class SignUpResponse(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val email: String,
)

@Serializable
data class SignUpError(
    @SerialName("first_name")
    val firstName: List<String>? = null,
    @SerialName("last_name")
    val lastName: List<String>? = null,
    val email: List<String>? = null,
    val password: List<String>? = null,
    val phone: List<String>? = null,
    @SerialName("non_field_errors")
    override val _genericErrors: List<String>? = null
) : ApiData.Error() {
    fun hasNonGenericErrors(): Boolean {
        return firstName.isNotNullOrEmpty() ||
                lastName.isNotNullOrEmpty() ||
                email.isNotNullOrEmpty() ||
                password.isNotNullOrEmpty() ||
                phone.isNotNullOrEmpty()
    }
}
