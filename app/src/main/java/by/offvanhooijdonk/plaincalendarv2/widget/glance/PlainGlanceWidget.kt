package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.content.Context
import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
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
import by.offvanhooijdonk.plaincalendar.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs.WidgetPrefsKeys
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.ConfigurationActivity
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

    @Composable
    override fun Content() {
        val state = currentState<Preferences>()

        val events = remember { mutableStateOf(emptyList<EventModel>()) }//viewModel.eventLiveData.observeAsState(emptyList())
        //LaunchedEffect(key1 = Unit) {
            events.value = viewModel.loadEvent(
                WidgetPrefsKeys.readCalendars(state),
                WidgetPrefsKeys.readDays(state)
            )
        //}

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
        LazyColumn() {
            items(events, itemId = { it.hashCode().toLong() }) { event ->
                Column(
                    modifier = GlanceModifier.clickable(actionStartActivity<ConfigurationActivity>(actionParametersOf())).padding(8.dp)
                ) {
                    Text(text = "Today at 12:30", style = TextStyle(color = textColor))
                    Row(modifier = GlanceModifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = GlanceModifier.size(12.dp).background(Color.White).cornerRadius(6.dp)) {}
                        Spacer(modifier = GlanceModifier.width(8.dp))
                        Text(text = event.title, style = TextStyle(color = textColor))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_WidgetBody() {
    //WidgetBody()
}

/*class WidgetSettingsStateDefinition : GlanceStateDefinition<WidgetModel> {
    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<WidgetModel> {
        TODO("Not yet implemented")
    }

    override fun getLocation(context: Context, fileKey: String): File {
        TODO("Not yet implemented")
    }
}*/
