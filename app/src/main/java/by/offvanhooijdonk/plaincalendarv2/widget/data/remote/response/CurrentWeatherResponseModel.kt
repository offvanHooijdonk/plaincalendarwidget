package by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response

import kotlinx.serialization.SerialName

data class CurrentWeatherResponseModel(
    val id: Int,
    /** kinda response code */
    val cod: Int,
    val weather: Weather,
    val main: MainData,
    val wind: Wind,
    val clouds: Clouds,
    /** distance */
    val visibility: Int,
    /** timezone ? */
    val timezone: Int,
    /** localized location name */
    val name: String,
    val sys: Sys,
    /** timestamp of ? */
    val dt: Long,
) {
    data class Weather(
        /** condition id */
        val id: Int,
        /** type of weather like 'Rain' */
        val main: String,
        /** human-readable description */
        val description: String,
        val icon: String,
    )

    data class Sys(
        /** country code, like "BY" */
        val country: String,
        /** timestamp */
        val sunrise: Long,
        /** timestamp */
        val sunset: Long,
    )

    data class MainData(
        val temp: Float,
        @SerialName("feels_like")
        val feelsLike: Float,
        @SerialName("temp_min")
        val tempMin: Float,
        @SerialName("temp_max")
        val tempMax: Float,
        val pressure: Int,
        val humidity: Int,
        /** pressure at the sea level */
        @SerialName("sea_level")
        val seaLevel: Int,
        /** pressure at the ground level */
        @SerialName("grnd_level")
        val groundLevel: Int,
    )

    data class Wind(
        val speed: Float,
        /** direction in degrees */
        val deg: Int,
        /** ??? */
        val gust: Float,
    )
    data class Clouds(
        val all: Int,
    )
}
