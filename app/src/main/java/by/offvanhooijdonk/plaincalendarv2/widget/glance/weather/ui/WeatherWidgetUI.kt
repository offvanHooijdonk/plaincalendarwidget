package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.WeatherWidgetViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.glanceDimens

@Composable
fun WeatherWidgetUI(state: WeatherWidgetViewModel.State) {
    Box(
        modifier = GlanceModifier
            .padding(horizontal = glanceDimens().widgetPaddingH, vertical = glanceDimens().widgetPaddingV)
            //.background(backColor.copy(alpha = opacity))
            .background(Color.LightGray)
            .appWidgetBackground()
            .fillMaxSize()
    ) {

        Column {
            Text(text = state.temp.toString(), style = TextStyle(fontSize = 26.sp)) // temperature
            Spacer(GlanceModifier.height(dimens().spacingS))

            Text(text = state.cityName, style = TextStyle(fontSize = 18.sp)) // city
        }
    }
}