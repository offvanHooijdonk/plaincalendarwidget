package by.offvanhooijdonk.plaincalendarv2.widget.data.remote

import by.offvanhooijdonk.plaincalendarv2.widget.BuildConfig
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response.CitiesSearchResponseModel
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response.CurrentWeatherResponseModel
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface WeatherApiService {
    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query(value = "q", encoded = true) query: String,
        @Query(value = "limit") limit: Int,
        @Query(value = "appid") apiKey: String = BuildConfig.OPENWEATHER_API_KEY,
    ): List<CitiesSearchResponseModel>

    @GET("data/2.5/weather")
    suspend fun loadForecast(
        @Query(value = "lat") lat: Float,
        @Query(value = "lon") lon: Float,
        @Query(value = "appid") apiKey: String = BuildConfig.OPENWEATHER_API_KEY,
        @Query(value = "lang") lang: String,
        @Query(value = "units") units: String = "metric", // Celsius
    ): CurrentWeatherResponseModel
}