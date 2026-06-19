package by.offvanhooijdonk.plaincalendarv2.widget.model.weather

import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response.CurrentWeatherResponseModel
import kotlin.math.roundToInt

data class WeatherModel(
    val tempValue: Float,
) {
    val temperature: String = tempValue.roundToInt().let { "$it°" }
}

fun CurrentWeatherResponseModel.toDomain() = WeatherModel(
    tempValue = main.temp
)