package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.job.CalendarChangeJobService
import by.offvanhooijdonk.plaincalendar.widget.helper.MILLIS_IN_DAY
import by.offvanhooijdonk.plaincalendar.widget.helper.closestMidnightMillis
import by.offvanhooijdonk.plaincalendarv2.widget.app.App.Companion.LOGCAT
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class PlainGlanceWidgetReceiver : GlanceAppWidgetReceiver(), KoinComponent {
    override val glanceAppWidget: PlainGlanceWidget = PlainGlanceWidget()

    private val alarmManager: AlarmManager by inject()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)

        CalendarChangeJobService.scheduleCalendarChangeJob(context)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(LOGCAT, "WidgetReceiver onUpdate")

        glanceAppWidget.loadData()
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d(LOGCAT, "WidgetReceiver onReceive action=${intent.action}")

        when (intent.action) {
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            INTENT_ACTION_NEW_DAY -> {
                PlainGlanceWidget().loadData()
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                PlainGlanceWidget().loadData()
                setupDailyAlarm(context)
            }
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        CalendarChangeJobService.cancelCalendarChangeJob(context)
        cancelDailyAlarm(context)
    }

    private fun setupDailyAlarm(ctx: Context) {
        alarmManager.setRepeating(
            AlarmManager.RTC,
            Calendar.getInstance().closestMidnightMillis,
            MILLIS_IN_DAY,
            getNewDayPendingIntent(ctx)
        )
    }

    private fun cancelDailyAlarm(ctx: Context) {
        alarmManager.cancel(getNewDayPendingIntent(ctx))
    }

    private fun getNewDayPendingIntent(ctx: Context): PendingIntent =
        Intent(ctx, PlainGlanceWidgetReceiver::class.java).let {
            it.action = INTENT_ACTION_NEW_DAY
            PendingIntent.getBroadcast(ctx, 0, it, 0)
        }


companion object {
    private const val INTENT_ACTION_NEW_DAY = "NEW_DAY_STARTED"
}
}
