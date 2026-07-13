package by.offvanhooijdonk.plaincalendarv2.widget.model.weather

import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response.CurrentWeatherResponseModel
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class WeatherModel(
    val tempValue: Float,
    val type: String,
    val description: String,
    val rain: Int?,
    val snow: Int?,
    val clouds: Int,
    val humidity: Int,
    val windSpeed: Float,
) {
    val temperature: String = tempValue.roundToInt().let { "$it°" }
}

fun CurrentWeatherResponseModel.toDomain() = WeatherModel(
    tempValue = main.temp,
    type = weather.lastOrNull()?.main ?: "",
    description = weather.lastOrNull()?.description ?: "",
    rain = rain?.hour?.toInt(),
    snow = snow?.hour?.toInt(),
    clouds = clouds.all,
    humidity = main.humidity,
    windSpeed = wind.speed,
)