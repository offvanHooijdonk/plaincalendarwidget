package by.offvanhooijdonk.plaincalendarv2.widget.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toColorLong

data class WeatherWidgetModel(
    override val backgroundColor: Long = Color.White.toColorLong(),
    override val textColor: Long = Color.White.toColorLong(),
    override val opacity: Float = 1f,
    override val textSizeDelta: Int = 0,
    override val textStyleBold: Boolean = false
) : ColorSettings
