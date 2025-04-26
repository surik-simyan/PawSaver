package com.pawsaver.app.core.network

import com.pawsaver.app.BuildKonfig
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.feature.login.data.ForgotPasswordBody
import com.pawsaver.app.feature.login.data.ForgotPasswordResponse
import com.pawsaver.app.feature.login.data.NewPasswordBody
import com.pawsaver.app.feature.login.data.NewPasswordResponse
import com.pawsaver.app.feature.login.data.TokenRefreshBody
import com.pawsaver.app.feature.login.data.TokenRefreshResponse
import com.pawsaver.app.feature.login.data.ResetPasswordBody
import com.pawsaver.app.feature.login.data.ResetPasswordResponse
import com.pawsaver.app.feature.login.data.ShelterSignUpBody
import com.pawsaver.app.feature.login.data.ShelterSignUpResponse
import com.pawsaver.app.feature.login.data.SignInBody
import com.pawsaver.app.feature.login.data.SignInResponse
import com.pawsaver.app.feature.login.data.UserSignUpBody
import com.pawsaver.app.feature.login.data.UserSignUpResponse
import com.pawsaver.app.feature.login.data.VerifyEmailBody
import com.pawsaver.app.feature.login.data.VerifyEmailResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PawsaverApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    private suspend inline fun <reified T> safeApiCall(
        crossinline apiCall: suspend () -> HttpResponse
    ): Result<ApiData.Response<T>> {
        return try {
            val response = apiCall()
            if (response.status.isSuccess()) {
                val successData = response.body<T>()
                Result.success(ApiData.Response(successData))
            } else {
                val errorBody = response.body<ApiData.Error>()
                Result.failure(CustomApiException(errorBody))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.body<ApiData.Error>()
            Result.failure(CustomApiException(errorBody))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<ApiData.Response<SignInResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/login") {
                setBody(SignInBody(email, password))
            }
        }
    }

    suspend fun refreshToken(token: String): Result<ApiData.Response<TokenRefreshResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/token-refresh") {
                setBody(TokenRefreshBody(token))
            }
        }
    }

    suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String
    ): Result<ApiData.Response<UserSignUpResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/register") {
                setBody(UserSignUpBody(firstName, lastName, email, password, phone))
            }
        }
    }

    suspend fun registerShelter(
        email: String,
        name: String,
        registrationNumber: String,
        phone: String,
        password: String,
    ): Result<ApiData.Response<ShelterSignUpResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/register_shelter") {
                setBody(ShelterSignUpBody(email, name, registrationNumber, phone, password))
            }
        }
    }

    suspend fun verify(
        email: String,
        code: String,
    ): Result<ApiData.Response<VerifyEmailResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/verify") {
                setBody(VerifyEmailBody(email, code))
            }
        }
    }

    suspend fun forgotPassword(
        email: String,
    ): Result<ApiData.Response<ForgotPasswordResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/password-reset") {
                setBody(ForgotPasswordBody(email))
            }
        }
    }

    suspend fun resetPassword(
        resetCode: String,
        encodedPk: String,
    ): Result<ApiData.Response<ResetPasswordResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/password-reset/verify-code/${encodedPk}") {
                setBody(ResetPasswordBody(resetCode))
            }
        }
    }

    suspend fun setNewPassword(
        newPassword: String,
        encodedPk: String,
        token: String,
    ): Result<ApiData.Response<NewPasswordResponse>> {
        return safeApiCall {
            httpClient.post("${BuildKonfig.API_URL}/password-reset-confirm/${encodedPk}/${token}") {
                setBody(NewPasswordBody(newPassword))
            }
        }
    }
}