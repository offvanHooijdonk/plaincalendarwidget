package by.offvanhooijdonk.plaincalendarv2.widget.di

import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.createWeatherApiService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 30_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 30_000
            }
            install(HttpSend)

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                }
                header(HttpHeaders.ContentType, ContentType.Application.Json)
            }
        }
    }
    single {
        Ktorfit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .httpClient(get<HttpClient>())
            .build()
    }
    single { get<Ktorfit>().createWeatherApiService() }
}