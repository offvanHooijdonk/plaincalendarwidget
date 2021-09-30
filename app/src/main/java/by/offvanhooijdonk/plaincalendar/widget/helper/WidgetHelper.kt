package by.offvanhooijdonk.plaincalendar.widget.helper

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import by.offvanhooijdonk.plaincalendar.widget.R
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel.Corners
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract

class WidgetHelper(
    private val ctx: Context,
    private val widgetsManager: AppWidgetManager,
) {
    fun getWidgetIds(clazz: Class<*>): IntArray =
        widgetsManager.getAppWidgetIds(ComponentName(ctx, clazz))

    fun notifyWidgetsDataChanged(widgetIds: IntArray) {
        widgetsManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents)
    }

/*
    fun notifyWidgetDataChanged(widgetId: Int) {
        widgetsManager.notifyAppWidgetViewDataChanged(widgetId, R.id.listEvents)
    }
*/

    fun getBackgroundRes(corners: Corners?): Int =
        when (corners) {
            Corners.NO_CORNER -> R.drawable.widget_back_no_corner
            Corners.SMALL -> R.drawable.widget_back_corner_s
            Corners.MEDIUM -> R.drawable.widget_back_corner_m
            Corners.LARGE -> R.drawable.widget_back_corner_l
            Corners.XLARGE -> R.drawable.widget_back_corner_xl
            else -> R.drawable.widget_back_corner_s
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
