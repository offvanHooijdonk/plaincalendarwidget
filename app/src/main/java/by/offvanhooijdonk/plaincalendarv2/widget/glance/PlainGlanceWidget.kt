package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.app.Activity
import android.util.Log
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.findModifier
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
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.ConfigurationActivity

class PlainGlanceWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<Preferences> = PreferencesGlanceStateDefinition

    @Composable
    override fun Content() {
        WidgetBody()
    }

}

@Composable
private fun WidgetBody() {
    val state = currentState<Preferences>()

    val color = WidgetPrefsKeys.readBackgroundColor(state)
    val opacity = WidgetPrefsKeys.readBackgroundOpacity(state)
    val textColor = ColorProvider(MaterialTheme.colors.onSurface)

    Box(modifier = GlanceModifier.background(color.copy(alpha = opacity)).appWidgetBackground().fillMaxSize()) {
        LazyColumn() {
            items(listOf("An event for today"), itemId = { it.hashCode().toLong() }) { event ->
                Column(
                    modifier = GlanceModifier.clickable(actionStartActivity<ConfigurationActivity>(actionParametersOf())).padding(8.dp)
                ) {
                    Text(text = "Today at 12:30", style = TextStyle(color = textColor))
                    Row(modifier = GlanceModifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = GlanceModifier.size(12.dp).background(Color.White).cornerRadius(6.dp)) {}
                        Spacer(modifier = GlanceModifier.width(8.dp))
                        Text(text = event, style = TextStyle(color = textColor))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_WidgetBody() {
    WidgetBody()
}

/*class WidgetSettingsStateDefinition : GlanceStateDefinition<WidgetModel> {
    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<WidgetModel> {
        TODO("Not yet implemented")
    }

    override fun getLocation(context: Context, fileKey: String): File {
        TODO("Not yet implemented")
    }
}*/
