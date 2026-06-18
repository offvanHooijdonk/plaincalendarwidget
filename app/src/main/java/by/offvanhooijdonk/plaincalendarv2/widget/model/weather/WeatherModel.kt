package by.offvanhooijdonk.plaincalendarv2.widget.model.weather

import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response.CurrentWeatherResponseModel
import java.text.DecimalFormat

data class WeatherModel(
    val tempValue: Float,
) {
    val temperature: String = formatter.format(tempValue).let { "$it°" }

    companion object {
        private val formatter = DecimalFormat("#.#")
    }
}

fun CurrentWeatherResponseModel.toDomain() = WeatherModel(
    tempValue = main.temp
)