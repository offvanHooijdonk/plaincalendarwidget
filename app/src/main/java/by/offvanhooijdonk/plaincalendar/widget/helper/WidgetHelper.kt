package by.offvanhooijdonk.plaincalendar.widget.helper

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.widget.CalendarWidgetProvider

class WidgetHelper(
    private val ctx: Context,
    private val widgetsManager: AppWidgetManager,
) {
    fun getExistingWidgetsIds(): IntArray =
        widgetsManager.getAppWidgetIds(ComponentName(ctx, CalendarWidgetProvider::class.java))

    fun notifyWidgetsDataChanged(widgetIds: IntArray) {
        widgetsManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents)
    }

    fun riseTextSizeBy(resId: Int, delta: Int): Float {
        return ctx.resources.getDimension(resId) / ctx.resources.displayMetrics.density + delta
    }

    fun createCalendarOpenIntent(): PendingIntent {
        val timeNow = System.currentTimeMillis()
        val intentCalendar = Intent(Intent.ACTION_VIEW)
        intentCalendar.data = CalendarContract.CONTENT_URI.buildUpon().appendPath("time").appendPath(timeNow.toString()).build()
        return PendingIntent.getActivity(ctx, 0, intentCalendar, 0)
    }

    fun createEventIntent(eventId: Long): Intent {
        val intent = Intent()
        intent.data = CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(eventId.toString()).build()
        return intent
    }
}
