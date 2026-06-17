package by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponseModel(
    val id: Int,
    /** kinda response code */
    val cod: Int,
    val weather: List<Weather>,
    val main: MainData,
    val wind: Wind,
    val clouds: Clouds,
    val rain: Falls? = null,
    val snow: Falls? = null,
    /** Distance */
    val visibility: Int,
    /** Timezone in seconds */
    val timezone: Int,
    /** Localized location name */
    val name: String,
    val sys: Sys,
    /** Timestamp of weather calculations */
    val dt: Long,
) {
    @Serializable
    data class Weather(
        /** condition id */
        val id: Int,
        /** type of weather like 'Rain' */
        val main: String,
        /** human-readable description */
        val description: String,
        val icon: String,
    )

    @Serializable
    data class Sys(
        /** country code, like "BY" */
        val country: String,
        /** timestamp */
        val sunrise: Long,
        /** timestamp */
        val sunset: Long,
    )

    @Serializable
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

    @Serializable
    data class Wind(
        val speed: Float,
        /** direction in degrees */
        val deg: Int,
        /** speed on blows */
        val gust: Float,
    )
    @Serializable
    data class Clouds(
        val all: Int,
    )
    @Serializable
    data class Falls(
        @SerialName("1h")
        val hour: Float,
    )
}
