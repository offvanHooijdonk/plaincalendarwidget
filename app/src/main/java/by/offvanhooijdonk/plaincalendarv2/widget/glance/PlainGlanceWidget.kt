package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs.WidgetPrefsKeys
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class PlainGlanceWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<Preferences> = PreferencesGlanceStateDefinition

    private val viewModel: WidgetViewModel by inject()
    private val context: Context by inject()
    private val coroutineScope = MainScope()

    @Composable
    override fun Content() {
        val state = currentState<Preferences>()

        val events = remember { mutableStateOf(emptyList<EventModel>()) }

        events.value = viewModel.loadEvents(
            WidgetPrefsKeys.readCalendars(state),
            WidgetPrefsKeys.readDays(state)
        )

        WidgetBody(events.value)
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

@Composable
private fun WidgetBody(events: List<EventModel>) {
    val state = currentState<Preferences>()

    val color = WidgetPrefsKeys.readBackgroundColor(state)
    val opacity = WidgetPrefsKeys.readBackgroundOpacity(state)
    val textColor = ColorProvider(MaterialTheme.colors.onSurface)

    Box(modifier = GlanceModifier.background(color.copy(alpha = opacity)).appWidgetBackground().fillMaxSize()) {
        LazyColumn {
            items(events, itemId = { it.hashCode().toLong() }) { event ->
                Column(
                    modifier = GlanceModifier.clickable(actionStartActivity(createOpenEventIntent(event.eventId))).padding(8.dp)
                ) {
                    Text(
                        text = event.dateStart.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault()),
                        style = TextStyle(color = textColor) // todo use 1 object
                    )
                    Row(modifier = GlanceModifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = GlanceModifier
                                .size(10.dp)
                                .background(event.eventColor?.let { Color(it.toLong()) } ?: Color.White)
                                .cornerRadius(6.dp)
                        ) {}
                        Spacer(modifier = GlanceModifier.width(8.dp))
                        Text(text = event.title, style = TextStyle(color = textColor), maxLines = 1)
                    }
                }
            }
        }
    }
}

private fun createOpenEventIntent(eventId: Long) =
    Intent(Intent.ACTION_VIEW).apply { data = CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(eventId.toString()).build() }

@Preview
@Composable
private fun Preview_WidgetBody() {
    //WidgetBody()
}
