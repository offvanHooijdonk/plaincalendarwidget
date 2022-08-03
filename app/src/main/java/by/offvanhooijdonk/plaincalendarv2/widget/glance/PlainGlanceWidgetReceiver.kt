package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.job.CalendarChangeJobService
import by.offvanhooijdonk.plaincalendarv2.widget.app.App.Companion.LOGCAT

class PlainGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: PlainGlanceWidget = PlainGlanceWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(LOGCAT, "WidgetReceiver onUpdate")

        glanceAppWidget.loadData()
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(LOGCAT, "WidgetReceiver onReceive")
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        CalendarChangeJobService.scheduleCalendarChangeJob(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        CalendarChangeJobService.cancelCalendarChangeJob(context)
    }
}
