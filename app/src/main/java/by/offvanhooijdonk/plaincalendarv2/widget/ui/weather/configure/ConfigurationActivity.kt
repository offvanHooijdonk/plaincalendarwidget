package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.ConfigureViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.FinishResult
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.extractWidgetId
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel by viewModel<WeatherConfigureViewModel>()

    private val widgetId = extractWidgetId()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        lifecycleScope.launch {
            viewModel.finishScreen.collect {
                val intent = Intent().apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                }
                setResult(if (it == FinishResult.OK) RESULT_OK else RESULT_CANCELED, intent)

                finish()
            }
        }
    }
}