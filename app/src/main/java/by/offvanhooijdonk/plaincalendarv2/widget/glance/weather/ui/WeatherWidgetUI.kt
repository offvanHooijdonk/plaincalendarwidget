package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.ui

import android.R.attr.contentDescription
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.WeatherWidgetViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.glanceDimens

@Composable
fun WeatherWidgetUI(state: WeatherWidgetViewModel.State) {
    Box(
        modifier = GlanceModifier
            .padding(horizontal = glanceDimens().widgetPaddingH, vertical = glanceDimens().widgetPaddingV)
            .background(state.widget.backColor)
            .appWidgetBackground()
            .fillMaxSize()
    ) {
        val contentColor = state.widget.textColor.toColor()
        val baseStyle = TextStyle(color = ColorProvider(contentColor))

        CompositionLocalProvider(
            LocalTextStyle provides baseStyle,
            LocalTintColor provides ColorFilter.tint(ColorProvider(contentColor))
        ) {
            state.weather?.let { weather ->
                Row(GlanceModifier.fillMaxWidth()) {
                    val size: DpSize = LocalSize.current // 296 * 203 (4 * 2)
                    val totalWidth = size.width - (glanceDimens().widgetPaddingH * 2)
                    val mainWidth = totalWidth / (MainWidthProp + AdditionalWidthProp) * MainWidthProp

                    TodayMain(
                        modifier = GlanceModifier
                            .width(mainWidth)
                            .fillMaxHeight(),
                        weather = weather,
                        widget = state.widget,
                    )
                    Spacer(GlanceModifier.width(glanceDimens().spacingM))

                    Column(
                        modifier = GlanceModifier
                            //.width(totalWidth - mainWidth)
                            .defaultWeight()
                            .fillMaxHeight(),
                    ) {
                        Spacer(GlanceModifier.height(glanceDimens().spacingS))

                        Box(GlanceModifier.fillMaxWidth().padding(end = dimens().spacingM), contentAlignment = Alignment.CenterEnd) {
                            ReloadTimeInfo(timeText = "12:45")
                        }
                        Spacer(GlanceModifier.height(glanceDimens().spacingXL + glanceDimens().spacingL))

                        TodayAdditionalInfo()
                        Spacer(GlanceModifier.height(glanceDimens().spacingXXL + glanceDimens().spacingXS))

                        Forecast()
                    }
                }
            }
        }
    }
}

private const val MainWidthProp = 6f
private const val AdditionalWidthProp = 5f

@Composable
private fun TodayMain(
    modifier: GlanceModifier,
    weather: WeatherModel,
    widget: WeatherWidgetModel,
) {
    Column(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        /*Row(verticalAlignment = Alignment.CenterVertically, horizontalAlignment = Alignment.End) {
            Text(text = "-${weather.temperature}", style = LocalTextStyle.current.copy(fontSize = 56.sp))
            Image(
                modifier = GlanceModifier.size(48.dp),
                provider = ImageProvider(R.drawable.we_cloudy),
                contentDescription = null,
            )
        }*/
        Box(modifier = GlanceModifier.fillMaxWidth().padding(end = dimens().spacingS), contentAlignment = Alignment.CenterEnd) {
            Text(
                modifier = GlanceModifier.padding(end = 42.dp),
                text = weather.temperature,
                style = LocalTextStyle.current.copy(fontSize = 52.sp),
            )
            Image(
                modifier = GlanceModifier.size(48.dp),/*.offset(x = (-4).dp)*/
                provider = ImageProvider(R.drawable.we_cloudy),
                colorFilter = LocalTintColor.current,
                //tint = Color.Unspecified,
                contentDescription = null,
            )
        }

        //Spacer(GlanceModifier.height(glanceDimens().spacingSM))

        Text(
            modifier = GlanceModifier.padding(start = dimens().spacingS),
            text = "Mostly cloudy",
            style = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            ),
            maxLines = 2,
            //overflow = TextOverflow.Ellipsis,
        )
        Spacer(GlanceModifier.height(glanceDimens().spacingXS))

        Text(
            modifier = GlanceModifier.padding(start = dimens().spacingS),
            text = widget.location.title.takeIf { it.isNotBlank() } ?: "--",
            style = LocalTextStyle.current.copy(fontSize = 12.sp),
            maxLines = 1,
            //overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TodayAdditionalInfo(modifier: GlanceModifier = GlanceModifier) {
    Row(modifier = modifier) {
        Column {
            InfoBadge(R.drawable.we_windy, "3 m/s")
            InfoBadge(R.drawable.we_cloudy, "80%")
        }
        Spacer(GlanceModifier.width(glanceDimens().spacingML))

        Column {
            InfoBadge(R.drawable.we_humidity, "58%")
            InfoBadge(R.drawable.we_heavy_rain, "20%")
        }
    }
}

@Composable
private fun Forecast(modifier: GlanceModifier = GlanceModifier) {
    Row(modifier = modifier) {
        ForecastBadge(
            time = "14:00",
            temperature = "22°",
            icon = R.drawable.we_sunny,
            rainProbability = 10,
        )
        Spacer(GlanceModifier.width(glanceDimens().spacingML))

        ForecastBadge(
            time = "18:00",
            temperature = "21°",
            icon = R.drawable.we_mostly_sunny,
            rainProbability = 0,
        )
        Spacer(GlanceModifier.width(glanceDimens().spacingML))

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
    modifier: GlanceModifier = GlanceModifier,
    timeText: String,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = GlanceModifier.size(glanceDimens().spacingML),
            provider = ImageProvider(R.drawable.ic_reload),
            colorFilter = LocalTintColor.current,
            contentDescription = null,
        )
        Spacer(GlanceModifier.width(glanceDimens().spacingXS))

        Text(timeText, style = LocalTextStyle.current.copy(fontSize = 10.sp))
    }
}

@Composable
fun InfoBadge(
    @DrawableRes icon: Int,
    text: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = GlanceModifier.size(glanceDimens().spacingL),
            provider = ImageProvider(icon),
            colorFilter = LocalTintColor.current,
            contentDescription = null,
        )
        Spacer(GlanceModifier.width(glanceDimens().spacingS))

        Text(text = text, style = LocalTextStyle.current.copy(fontSize = 12.sp))
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
        Text(time, style = LocalTextStyle.current.copy(fontSize = 10.sp/*, lineHeight = 18.sp*/))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(temperature, style = LocalTextStyle.current.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold))
            Spacer(GlanceModifier.width(glanceDimens().spacingXS))

            Image(
                modifier = GlanceModifier.size(12.dp),
                provider = ImageProvider(icon),
                //tint = Color.Unspecified,
                contentDescription = null,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = GlanceModifier.size(10.dp),
                provider = ImageProvider(R.drawable.we_heavy_rain),
                colorFilter = LocalTintColor.current,
                contentDescription = null,
            )
            Spacer(GlanceModifier.width(glanceDimens().spacingXS))

            Text("$rainProbability%", style = LocalTextStyle.current.copy(fontSize = 11.sp/*, lineHeight = 18.sp*/))
        }
    }
}

val LocalTextStyle = compositionLocalOf { TextStyle() }
val LocalTintColor = compositionLocalOf { ColorFilter.tint(ColorProvider(Color.Unspecified)) }
