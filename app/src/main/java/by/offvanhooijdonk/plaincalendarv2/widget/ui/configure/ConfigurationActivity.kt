package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.MainScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfigurationActivity : AppCompatActivity() {
    private val viewModel: ConfigureViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.passWidgetId(extractWidgetId())

        setContent {
            MaterialTheme {
                MainScreen(viewModel)
            }
        }
    }

    private fun extractWidgetId(): Int? =
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
}
