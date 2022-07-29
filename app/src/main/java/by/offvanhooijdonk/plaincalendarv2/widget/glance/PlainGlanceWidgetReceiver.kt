package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class PlainGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: PlainGlanceWidget = PlainGlanceWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

    }
}
