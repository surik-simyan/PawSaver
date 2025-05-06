package com.pawsaver.app.core.network

import com.pawsaver.app.BuildKonfig
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.feature.login.data.ForgotPasswordBody
import com.pawsaver.app.feature.login.data.ForgotPasswordResponse
import com.pawsaver.app.feature.login.data.NewPasswordBody
import com.pawsaver.app.feature.login.data.NewPasswordResponse
import com.pawsaver.app.feature.login.data.ResetPasswordBody
import com.pawsaver.app.feature.login.data.ResetPasswordResponse
import com.pawsaver.app.feature.login.data.ShelterSignUpBody
import com.pawsaver.app.feature.login.data.ShelterSignUpResponse
import com.pawsaver.app.feature.login.data.SignInBody
import com.pawsaver.app.feature.login.data.SignInResponse
import com.pawsaver.app.feature.login.data.TokenRefreshBody
import com.pawsaver.app.feature.login.data.TokenRefreshResponse
import com.pawsaver.app.feature.login.data.UserSignUpBody
import com.pawsaver.app.feature.login.data.UserSignUpResponse
import com.pawsaver.app.feature.login.data.VerifyEmailBody
import com.pawsaver.app.feature.login.data.VerifyEmailResponse
import com.pawsaver.app.feature.main.data.ListingsResponse
import com.pawsaver.app.feature.main.data.ProfileResponse
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

class PawsaverApi(
    private val settings: Settings
) {
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
        install(Logging) {
            logger = object : KtorLogger {
                override fun log(message: String) {
                    KermitLogger.i(message)
                }
            }
            level = LogLevel.ALL
        }
//        install(Auth) {
//            bearer {
//                sendWithoutRequest { request ->
//                    LOGIN_ENDPOINTS.any { request.url.encodedPath.startsWith(it) }
//                }
//                refreshTokens {
//                    val token = client.post {
//                        markAsRefreshTokenRequest()
//                        url("${BuildKonfig.API_URL}/token-refresh")
//                        setBody(TokenRefreshBody(settings.getString("refresh_token", "")))
//                    }.body<TokenRefreshResponse>()
//                    BearerTokens(token.access, token.refresh)
//                }
//                loadTokens {
//                    BearerTokens(
//                        settings.getString("access_token", ""),
//                        settings.getString("refresh_token", "")
//                    )
//                }
//            }
//        }
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
            httpClient.post(LOGIN_ENDPOINT) {
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
            httpClient.post(REGISTER_ENDPOINT) {
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
            httpClient.post(REGISTER_SHELTER_ENDPOINT) {
                setBody(ShelterSignUpBody(email, name, registrationNumber, phone, password))
            }
        }
    }

    suspend fun verify(
        email: String,
        code: String,
    ): Result<ApiData.Response<VerifyEmailResponse>> {
        return safeApiCall {
            httpClient.post(VERIFY_ENDPOINT) {
                setBody(VerifyEmailBody(email, code))
            }
        }
    }

    suspend fun forgotPassword(
        email: String,
    ): Result<ApiData.Response<ForgotPasswordResponse>> {
        return safeApiCall {
            httpClient.post(FORGOT_PASSWORD_ENDPOINT) {
                setBody(ForgotPasswordBody(email))
            }
        }
    }

    suspend fun resetPassword(
        resetCode: String,
        encodedPk: String,
    ): Result<ApiData.Response<ResetPasswordResponse>> {
        return safeApiCall {
            httpClient.post("$RESET_PASSWORD_ENDPOINT/${encodedPk}") {
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
            httpClient.post("$SET_NEW_PASSWORD_ENDPOINT/${encodedPk}/${token}") {
                setBody(NewPasswordBody(newPassword))
            }
        }
    }

    suspend fun listings(page: Int): Result<ApiData.Response<ListingsResponse>> {
        return safeApiCall {
            httpClient.get(LISTINGS_ENDPOINT) {
                parameter("page", page)
                header("Authorization", "Bearer ${settings.getString("accessToken", "")}")
            }
        }
    }

    suspend fun profile(): Result<ApiData.Response<ProfileResponse>> {
        return safeApiCall {
            httpClient.get(PROFILE_ENDPOINT) {
                header("Authorization", "Bearer ${settings.getString("accessToken", "")}")
            }
        }
    }

    companion object {
        val LOGIN_ENDPOINT = "${BuildKonfig.API_URL}/login"
        val REGISTER_ENDPOINT = "${BuildKonfig.API_URL}/register"
        val REGISTER_SHELTER_ENDPOINT = "${BuildKonfig.API_URL}/register_shelter"
        val VERIFY_ENDPOINT = "${BuildKonfig.API_URL}/verify"
        val FORGOT_PASSWORD_ENDPOINT = "${BuildKonfig.API_URL}/password-reset"
        val RESET_PASSWORD_ENDPOINT = "${BuildKonfig.API_URL}/password-reset/verify-code"
        val SET_NEW_PASSWORD_ENDPOINT = "${BuildKonfig.API_URL}/password-reset-confirm"

        val LOGIN_ENDPOINTS = listOf(
            LOGIN_ENDPOINT,
            REGISTER_ENDPOINT,
            REGISTER_SHELTER_ENDPOINT,
            VERIFY_ENDPOINT,
            FORGOT_PASSWORD_ENDPOINT,
            RESET_PASSWORD_ENDPOINT,
            SET_NEW_PASSWORD_ENDPOINT
        )

        val LISTINGS_ENDPOINT = "${BuildKonfig.API_URL}/user/listings"
        val PROFILE_ENDPOINT = "${BuildKonfig.API_URL}/user/me"

    }
}