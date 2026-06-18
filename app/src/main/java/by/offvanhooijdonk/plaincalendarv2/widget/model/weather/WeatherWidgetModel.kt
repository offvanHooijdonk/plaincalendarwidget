package by.offvanhooijdonk.plaincalendarv2.widget.model.weather

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.ColorSettings

data class WeatherWidgetModel(
    override val backgroundColor: Long = Color.White.toColorLong(),
    override val textColor: Long = Color.Black.toColorLong(),
    override val opacity: Float = 1f,
    override val textSizeDelta: Int = 0,
    override val textStyleBold: Boolean = false,
    val id: Long = 0,
    val location: LocationModel = LocationModel(
        title = "",
        countryCode = "",
        state = null,
        lat = 0.0,
        lon = 0.0,
    ),
) : ColorSettings {
    val backColor: Color = backgroundColor.toColor().copy(alpha = opacity)
}