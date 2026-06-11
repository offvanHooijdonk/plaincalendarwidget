package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.height
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WeatherGlanceWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<Preferences> = PreferencesGlanceStateDefinition

    private val viewModel: WeatherWidgetViewModel by inject()
    private val context: Context by inject()
    private val coroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val state = viewModel.state.collectAsState().value
            viewModel.reload()
            Column {
                Text(text = state.cityName, style = TextStyle(fontSize = 18.sp)) // city
                Spacer(GlanceModifier.height(dimens().spacingS))

                Text(text = state.temp.toString(), style = TextStyle(fontSize = 26.sp)) // temperature
            }
        }
    }

    fun loadData() {
        coroutineScope.launch {
            updateAll(context)
        }
    }
}