package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import org.koin.core.component.KoinComponent

class WeatherGlanceWidget : GlanceAppWidget(), KoinComponent {
    override val stateDefinition: GlanceStateDefinition<Preferences> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        TODO("Not yet implemented")
    }
}