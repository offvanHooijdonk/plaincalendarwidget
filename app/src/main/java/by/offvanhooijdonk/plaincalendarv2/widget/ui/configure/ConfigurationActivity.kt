package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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

        viewModel.finishScreen.observe(this) {
            if (it != null) {
                viewModel.widgetId?.let { widgetId ->
                    val intent = Intent().apply {
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                    }
                    setResult(if (it == ConfigureViewModel.FinishResult.OK) RESULT_OK else RESULT_CANCELED, intent)
                }

                finish()
            }
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onAction(ConfigureViewModel.Action.OnPackPressed)
            }
        })
    }

    private fun extractWidgetId(): Int? =
        intent.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
}
