package by.offvanhooijdonk.plaincalendarv2.widget.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CitiesSearchResponseModel(
    val name: String,
    val lat: Float,
    val lon: Float,
    val country: String,
    @SerialName("local_names")
    val localNames: Map<String, String>?,
)

