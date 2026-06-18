package by.offvanhooijdonk.plaincalendarv2.widget.model.weather

data class LocationModel(
    val title: String,
    val countryCode: String,
    val state: String?,
    val lat: Double,
    val lon: Double,
)
