package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.MainScreen
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel: ConfigureViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.passWidgetId(extractWidgetId())

        setContent {
            PlainTheme {
                MainScreen(viewModel)
            }
        }

        viewModel.finishScreen.observe({ lifecycle }) {
            if (it) {
                viewModel.widgetId?.let { widgetId ->
                    val intent = Intent().apply {
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun extractWidgetId(): Int? =
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
}
