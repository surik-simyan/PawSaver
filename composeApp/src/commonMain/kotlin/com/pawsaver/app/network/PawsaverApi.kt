package com.pawsaver.app.network

import com.pawsaver.app.BuildKonfig
import com.pawsaver.app.data.SignInBody
import com.pawsaver.app.data.SignUpBody
import com.pawsaver.app.data.VerifyBody
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
    }

    suspend fun login(email: String, password: String): HttpResponse {
        return httpClient.post("${BuildKonfig.API_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(SignInBody(email, password))
        }
    }

    suspend fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phone: String
    ): HttpResponse {
        return httpClient.post("${BuildKonfig.API_URL}/register") {
            contentType(ContentType.Application.Json)
            setBody(SignUpBody(firstName, lastName, email, password, phone))
        }
    }

    suspend fun verify(
        email: String,
        code: String,
    ): HttpResponse {
        return httpClient.post("${BuildKonfig.API_URL}/verify") {
            contentType(ContentType.Application.Json)
            setBody(VerifyBody(email, code))
        }
    }
}