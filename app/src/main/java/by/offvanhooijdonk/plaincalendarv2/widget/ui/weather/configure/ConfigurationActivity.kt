package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.FinishResult
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.extractWidgetId
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel by viewModel<WeatherConfigureViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("===", "onCreate")
        val widgetId = extractWidgetId()
        Log.d("===", "Widget ID : $widgetId")

        setContent {
            PlainTheme {
                WeatherConfigureScreen(viewModel.state.collectAsState().value, viewModel::onIntent)
            }
        }

        lifecycleScope.launch {
            viewModel.finishScreen.collect { result ->
                result?.let {
                    val intent = Intent().apply {
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    }
                    setResult(if (result == FinishResult.OK) RESULT_OK else RESULT_CANCELED, intent)

                    finish()
                }
            }
        }
    }
}