package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.preview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.model.WeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure.LocationModel

@Composable
fun WeatherWidgetPreview(
    modifier: Modifier,
    widget: WeatherWidgetModel,
) {
    val location = LocationModel(
        title = "Gomel",
        countryCode = "By",
        state = "Gomel region",
        lat = 0.0,
        lon = 0.0,
    )
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(widget.backgroundColor), contentColor = Color(widget.textColor)
        ),
    ) {
        Text(text = "18.6°")
        Spacer(Modifier.height(dimens().spacingS))

        Text(text = location.title)
    }
}

@Preview(device = Devices.PIXEL_9, showBackground = true)
@Composable
private fun Preview_WeatherWidgetPreview() {
    PlainTheme {
        WeatherWidgetPreview(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(dimens().spacingL),
            widget = WeatherWidgetModel()
        )
    }
}