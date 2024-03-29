package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.currentState
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs.readWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.widgetui.WidgetBodyPerDay
import by.offvanhooijdonk.plaincalendarv2.widget.glance.widgetui.WidgetBodyTimeline
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlainGlanceWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<Preferences> = PreferencesGlanceStateDefinition

    private val viewModel: WidgetViewModel by inject()
    private val context: Context by inject()
    private val coroutineScope = MainScope()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val state = currentState<Preferences>()

        val events = remember { mutableStateOf(emptyList<EventModel>()) }

        val widgetModel = state.readWidgetModel()
        events.value = viewModel.loadEvents(
            widgetModel.calendarIds,
            widgetModel.days
        )

        when(widgetModel.layoutType) {
            WidgetModel.LayoutType.TIMELINE -> WidgetBodyTimeline(events.value, widgetModel)
            WidgetModel.LayoutType.PER_DAY -> WidgetBodyPerDay(events.value, widgetModel)
        }
    }

    fun loadData() {
        Log.d(App.LOGCAT, "PlainWidget loadData()")
        coroutineScope.launch {
            updateAll(context)
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)

        coroutineScope.cancel()
    }
}
