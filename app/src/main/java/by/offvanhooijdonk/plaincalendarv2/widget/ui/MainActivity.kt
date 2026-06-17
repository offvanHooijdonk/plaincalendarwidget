@file:OptIn(ExperimentalMaterial3Api::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.ConfigurationActivity as CalendarActivity
import by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure.ConfigurationActivity as WeatherActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlainTheme {
                WidgetPickScreen(
                    onWidgetSelect = { widget ->
                        when (widget) {
                            Widget.CALENDAR -> startActivity(Intent(this, CalendarActivity::class.java))
                            Widget.WEATHER -> startActivity(Intent(this, WeatherActivity::class.java))
                        }
                    }
                )
            }
        }
    }

    enum class Widget {
        CALENDAR, WEATHER
    }
}

@Composable
private fun WidgetPickScreen(onWidgetSelect: (MainActivity.Widget) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
        }
    ) { innerPaddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            WidgetButton(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(dimens().widgetSelectionCardHeight),
                text = stringResource(R.string.calendar_widget_title),
                onClick = { onWidgetSelect(MainActivity.Widget.CALENDAR) },
            )
            Spacer(Modifier.height(dimens().spacingL))

            WidgetButton(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(dimens().widgetSelectionCardHeight),
                text = stringResource(R.string.weather_widget_title),
                onClick = { onWidgetSelect(MainActivity.Widget.WEATHER) },
            )
        }
    }
}

@Composable
fun WidgetButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    OutlinedCard(modifier = modifier, onClick = onClick) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_9)
@Composable
private fun Preview_WidgetPickScreen() {
    PlainTheme {
        WidgetPickScreen(onWidgetSelect = {})
    }
}