package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.preview

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
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
    weather: WeatherModel,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = widget.backColor,
            contentColor = widget.textColor.toColor()
        ),
    ) {
        Row(Modifier.padding(horizontal = dimens().spacingM, vertical = dimens().spacingM)) {
            TodayMain(
                modifier = Modifier
                    .weight(11f)
                    .padding(start = dimens().spacingM)
                    .fillMaxHeight(),
                weather = weather,
                widget = widget,
            )
            //Spacer(Modifier.width(dimens().spacingS))

            Column(
                modifier = Modifier
                    .weight(10f)
                    .fillMaxHeight(),
                //verticalArrangement = Arrangement.Center,
            ) {
                ReloadTimeInfo(Modifier.align(Alignment.End), "12:45")
                Spacer(Modifier.height(dimens().spacingML))

                TodayAdditionalInfo()
                Spacer(Modifier.height(dimens().spacingL + dimens().spacingXS))

                Forecast()
            }
        }
    }
}

@Composable
private fun TodayMain(
    modifier: Modifier,
    weather: WeatherModel,
    widget: WeatherWidgetModel,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = weather.temperature, fontSize = 56.sp)
            Icon(
                modifier = Modifier.size(48.dp).offset(x = (-4).dp),
                painter = painterResource(R.drawable.we_cloudy),
                tint = Color.Unspecified,
                contentDescription = null,
            )
        }
        Spacer(Modifier.height(dimens().spacingSM))

        Text(
            text = "Mostly cloudy",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(dimens().spacingXS))

        Text(
            text = widget.location.title.takeIf { it.isNotBlank() } ?: "--",
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TodayAdditionalInfo(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(dimens().spacingML)) {
        Column {
            InfoBadge(R.drawable.we_windy, "3 m/s")
            InfoBadge(R.drawable.we_cloudy, "80%")
        }
        Column {
            InfoBadge(R.drawable.we_humidity, "58%")
            InfoBadge(R.drawable.we_heavy_rain, "20%")
        }
    }
}

@Composable
private fun Forecast(modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(dimens().spacingML)) {
        ForecastBadge(
            time = "14:00",
            temperature = "22°",
            icon = R.drawable.we_sunny,
            rainProbability = 10,
        )
        ForecastBadge(
            time = "18:00",
            temperature = "21°",
            icon = R.drawable.we_mostly_sunny,
            rainProbability = 0,
        )
        ForecastBadge(
            time = "22:00",
            temperature = "18°",
            icon = R.drawable.we_partly_cloudy_night,
            rainProbability = 20,
        )
    }
}

@Composable
private fun ReloadTimeInfo(
    modifier: Modifier = Modifier,
    timeText: String,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(dimens().spacingML),
            painter = painterResource(R.drawable.ic_reload),
            contentDescription = null,
        )
        Spacer(Modifier.width(dimens().spacingXS))

        Text(timeText, fontSize = 10.sp)
    }
}

@Composable
fun InfoBadge(
    @DrawableRes icon: Int,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(dimens().spacingS)) {
        Icon(
            modifier = Modifier.size(dimens().spacingL),
            painter = painterResource(icon),
            contentDescription = null,
        )

        Text(text = text, fontSize = 12.sp)
    }
}

@Composable
fun ForecastBadge(
    time: String,
    temperature: String,
    @DrawableRes icon: Int,
    rainProbability: Int,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(time, fontSize = 10.sp, lineHeight = 18.sp)

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(dimens().spacingXS)) {
            Text(temperature, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(icon),
                tint = Color.Unspecified,
                contentDescription = null,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(dimens().spacingXS)) {
            Icon(
                modifier = Modifier.size(10.dp),
                painter = painterResource(R.drawable.we_heavy_rain),
                contentDescription = null,
            )

            Text("$rainProbability%", fontSize = 11.sp, lineHeight = 18.sp)
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
                .height(dimens().widgetPreviewHeight),
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
                tempValue = 20.0f,
            ),
        )
    }
}
