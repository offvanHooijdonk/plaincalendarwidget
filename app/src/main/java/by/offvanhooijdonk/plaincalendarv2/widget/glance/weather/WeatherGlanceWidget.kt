package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.prefs.readWeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.ui.WeatherWidgetUI
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.toIntId
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

class WeatherGlanceWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<Preferences> = WeatherStateDefinition

    private val viewModel: WeatherWidgetViewModel by inject()
    private val context: Context by inject()
    private val coroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = viewModel.state.collectAsState().value
            val prefs = currentState<Preferences>()
            val widgetSettings = remember { prefs.readWeatherWidgetModel() }

            LaunchedEffect(Unit) {
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