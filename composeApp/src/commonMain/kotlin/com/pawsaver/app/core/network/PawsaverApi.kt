package com.pawsaver.app.core.network

import com.pawsaver.app.BuildKonfig
import com.pawsaver.app.core.data.ApiData
import com.pawsaver.app.core.data.CustomApiException
import com.pawsaver.app.feature.login.data.SignInBody
import com.pawsaver.app.feature.login.data.SignInError
import com.pawsaver.app.feature.login.data.SignInResponse
import com.pawsaver.app.feature.login.data.SignUpBody
import com.pawsaver.app.feature.login.data.SignUpError
import com.pawsaver.app.feature.login.data.SignUpResponse
import com.pawsaver.app.feature.login.data.VerifyEmailBody
import com.pawsaver.app.feature.login.data.VerifyEmailError
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

    private suspend inline fun <reified T, reified E : ApiData.Error> safeApiCall(
        crossinline apiCall: suspend () -> HttpResponse
    ): Result<ApiData.Response<T>> {
        return try {
            val response = apiCall()
            if (response.status.isSuccess()) {
                val successData = response.body<T>()
                Result.success(ApiData.Response(successData))
            } else {
                val errorBody = response.body<E>()
                Result.failure(CustomApiException(errorBody))
            }
        } catch (e: ClientRequestException) {
            val errorBody = e.response.body<E>()
            Result.failure(CustomApiException(errorBody))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<ApiData.Response<SignInResponse>> {
        return safeApiCall<SignInResponse, SignInError> {
            httpClient.post("${BuildKonfig.API_URL}/login") {
                setBody(SignInBody(email, password))
            }
        }
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String
    ): Result<ApiData.Response<SignUpResponse>> {
        return safeApiCall<SignUpResponse, SignUpError> {
            httpClient.post("${BuildKonfig.API_URL}/register") {
                setBody(SignUpBody(firstName, lastName, email, password, phone))
            }
        }
    }

    suspend fun verify(
        email: String,
        code: String,
    ): Result<ApiData.Response<VerifyEmailResponse>> {
        return safeApiCall<VerifyEmailResponse, VerifyEmailError> {
            httpClient.post("${BuildKonfig.API_URL}/verify") {
                setBody(VerifyEmailBody(email, code))
            }
        }
    }
}