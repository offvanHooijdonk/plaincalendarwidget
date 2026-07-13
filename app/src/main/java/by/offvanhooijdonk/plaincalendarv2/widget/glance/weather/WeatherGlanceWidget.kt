package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.*
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.prefs.readWeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.ui.WeatherWidgetUI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class WeatherGlanceWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<Preferences> = WeatherStateDefinition
    override val sizeMode: SizeMode = SizeMode.Exact

    private val viewModel: WeatherWidgetViewModel by inject()
    private val context: Context by inject()
    private val coroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = viewModel.state.collectAsState().value
            val prefs = currentState<Preferences>()
            val widgetSettings = prefs.readWeatherWidgetModel() /*remember { prefs.readWeatherWidgetModel() }*/

            LaunchedEffect(widgetSettings) {
                widgetSettings?.let {
                    viewModel.setWidgetSettings(it)
                    viewModel.reload()
                }
            }

            WeatherWidgetUI(state)
        }
    }

    fun loadData() {
        coroutineScope.launch {
            updateAll(context)
        }
    }
}

object WeatherStateDefinition : GlanceStateDefinition<Preferences> {
    private const val FILE_NAME = "weather_widget_prefs"

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile("glance_${FILE_NAME}_$fileKey.preferences_pb")
    }

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { getLocation(context, fileKey) }
        )
    }
}
