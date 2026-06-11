package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import org.koin.core.component.KoinComponent

class WeatherWidgetReceiver :GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: WeatherGlanceWidget = WeatherGlanceWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        glanceAppWidget.loadData()
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        // TODO need this?
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)

        // todo stop scheduled updates
    }

}