package by.offvanhooijdonk.plaincalendarv2.widget.model.weather

import android.R.attr.timeZone
import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response.CurrentWeatherResponseModel
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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
    val updateTime: String,
) {
    val temperature: String = tempValue.roundToInt().let { "$it°" }
}

fun CurrentWeatherResponseModel.toDomain(): WeatherModel {
    val instant = Instant.fromEpochMilliseconds(dt)

    // Create TimeZone from seconds offset
    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

    // Extract date and time separately
    val time = localDateTime.time
    val javaTime = time.toJavaLocalTime()

    // 1. Automatically use the device's current default locale
    val defaultFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    return WeatherModel(
        tempValue = main.temp,
        type = weather.lastOrNull()?.main ?: "",
        description = weather.lastOrNull()?.description ?: "",
        rain = rain?.hour?.toInt(),
        snow = snow?.hour?.toInt(),
        clouds = clouds.all,
        humidity = main.humidity,
        windSpeed = wind.speed,
        updateTime = javaTime.format(defaultFormatter),
    )
}