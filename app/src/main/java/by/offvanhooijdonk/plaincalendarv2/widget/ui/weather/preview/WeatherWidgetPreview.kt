package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.preview

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.LocationModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens

@Composable
fun WeatherWidgetPreview(
    modifier: Modifier,
    widget: WeatherWidgetModel,
    weather: WeatherModel?,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = widget.backgroundColor.toColor().copy(alpha = widget.opacity),
            contentColor = widget.textColor.toColor()
        ),
    ) {
        Box(Modifier.padding(dimens().spacingL)) {
            Column() {
                Text(text = weather?.temperature ?: "--", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(dimens().spacingS))

                Text(text = widget.location?.title ?: "--")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_WeatherWidgetPreview() {
    PlainTheme {
        WeatherWidgetPreview(
            modifier = Modifier
                .width(dimens().widgetPreviewWidth)
                .height(dimens().widgetPreviewHeight)
                .padding(dimens().spacingL),
            widget = WeatherWidgetModel(
                location = LocationModel(
                    title = "Gomel",
                    countryCode = "BY",
                    state = "Gomel Region",
                    lat = 0.0,
                    lon = 0.0,
                )
            ),
            weather = WeatherModel(
                tempValue = 18.6f,
            ),
        )
    }
}
