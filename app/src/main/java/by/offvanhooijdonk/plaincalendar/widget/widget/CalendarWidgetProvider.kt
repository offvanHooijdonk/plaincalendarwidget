package by.offvanhooijdonk.plaincalendar.widget.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.CalendarsRemoteService
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.job.CalendarChangeJobService
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.observer.EventsContentObserver
import by.offvanhooijdonk.plaincalendar.widget.data.database.WidgetDao
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Deprecated("")
class CalendarWidgetProvider : AppWidgetProvider(), KoinComponent {
    private val contentObserver: EventsContentObserver? = null

    private val widgetDao: WidgetDao by inject()
    private val alarmManager: AlarmManager by inject()

    override fun onEnabled(ctx: Context) {
        super.onEnabled(ctx)
        Log.d(App.LOGCAT, "Enabled")
        setupDailyAlarm(ctx)

        CalendarChangeJobService.scheduleCalendarChangeJob(ctx)
    }

    override fun onUpdate(ctx: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(ctx, appWidgetManager, appWidgetIds)
        Log.d(App.LOGCAT, "Update " + appWidgetIds.contentToString())
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(ctx, CalendarsRemoteService::class.java)
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)))
            val rv = RemoteViews(ctx.packageName, R.layout.widget_calendars)
            /*widgetDao.getById(appWidgetId.toLong())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ widgetBean ->
                    Log.i(App.Companion.LOGCAT, "onUpdate. Corners " + widgetBean.getCorners())
                    val backResId: Int = WidgetHelper.getBackgroundRes(widgetBean.getCorners())
                    rv.setImageViewResource(R.id.widgetBack, backResId)
                    rv.setInt(R.id.widgetBack, "setColorFilter", widgetBean.getBackgroundColor())
                    rv.setInt(R.id.widgetBack, "setImageAlpha", widgetBean.getOpacity() * 0xFF / 100)
                    rv.setTextColor(R.id.txtWidgetDate, widgetBean.getTextColor())
                    rv.setTextColor(R.id.txtWidgetDay, widgetBean.getTextColor())
                    rv.setInt(R.id.viewDivider, "setColorFilter", widgetBean.getTextColor())
                    rv.setTextColor(R.id.emptyView, widgetBean.getTextColor())
                    rv.setTextViewTextSize(R.id.emptyView, TypedValue.COMPLEX_UNIT_SP, WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_event_title, widgetBean.getTextSizeDelta()))
                    updateDateViews(ctx, rv, widgetBean.getShowTodayLeadingZero())
                    rv.setRemoteAdapter(R.id.listEvents, intent)
                    rv.setEmptyView(R.id.listEvents, R.id.emptyView)
                    val intentTemplate = Intent(Intent.ACTION_VIEW)
                    rv.setPendingIntentTemplate(R.id.listEvents, PendingIntent.getActivity(ctx, 0, intentTemplate, 0))
                    val showDate: Boolean = widgetBean.getShowTodayDate()
                    rv.setViewVisibility(R.id.txtWidgetDate, if (showDate) View.VISIBLE else View.GONE)
                    rv.setViewVisibility(R.id.txtWidgetDay, if (showDate && widgetBean.getShowTodayDayOfWeek()) View.VISIBLE else View.GONE)
                    rv.setViewVisibility(R.id.viewDivider, if (showDate && widgetBean.getShowDateDivider()) View.VISIBLE else View.GONE)
                    appWidgetManager.updateAppWidget(appWidgetId, rv)
                    WidgetHelper.notifyWidgetDataChanged(ctx, widgetBean.getId() as Int)
                }) { th -> }*/
        }
    }

    override fun onReceive(ctx: Context, intent: Intent) {
        super.onReceive(ctx, intent)
        Log.d(App.Companion.LOGCAT, "onReceive")
        Log.d(App.Companion.LOGCAT, "Got " + intent.getAction() + " action")
        if (Intent.ACTION_DATE_CHANGED == intent.getAction() || Intent.ACTION_TIME_CHANGED == intent.getAction() || Intent.ACTION_TIMEZONE_CHANGED == intent.getAction() || INTENT_ACTION_NEW_DAY == intent.getAction() || Intent.ACTION_BOOT_COMPLETED == intent.getAction()) {
            /*val widgetIds: IntArray = WidgetHelper.getWidgetIds(ctx, CalendarWidgetProvider::class.java)
            Log.i(App.Companion.LOGCAT, "Applying current date on Widgets")

            // TODO send update Broadcast instead?
            WidgetHelper.notifyWidgetsDataChanged(ctx, widgetIds)
            val manager: AppWidgetManager = AppWidgetManager.getInstance(ctx)
            for (widgetId in widgetIds) {
                Log.i(App.Companion.LOGCAT, "Widget $widgetId")
                val rv = RemoteViews(ctx.packageName, R.layout.widget_calendars)
                // TODO update views basing on WidgetBean
                updateDateViews(ctx, rv, false)
                manager.partiallyUpdateAppWidget(widgetId, rv)
            }
            if (Intent.ACTION_BOOT_COMPLETED == intent.getAction()) {
                setupDailyAlarm(ctx)
            }*/
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        Log.i(App.Companion.LOGCAT, "Deleted")
    }

    override fun onDisabled(ctx: Context) {
        super.onDisabled(ctx)
        Log.i(App.Companion.LOGCAT, "Disabled")

        // TODO check that it is here to be unsubscribed
        if (contentObserver != null) {
            ctx.contentResolver.unregisterContentObserver(contentObserver)
        }

        alarmManager.cancel(getNewDayPendingIntent(ctx))
    }

    private fun getNewDayPendingIntent(ctx: Context): PendingIntent {
        val intent = Intent(ctx, CalendarWidgetProvider::class.java)
        intent.setAction(INTENT_ACTION_NEW_DAY)
        // replace with a method that returns a *new* one
        return PendingIntent.getBroadcast(ctx, 0, intent, 0)
    }

    /*private fun updateDateViews(ctx: Context, rv: RemoteViews, showLeadingZero: Boolean) {
        val now = Date(System.currentTimeMillis())
        rv.setTextViewText(R.id.txtWidgetDate, DateHelper.formatDateOnly(now, showLeadingZero))
        rv.setTextViewText(R.id.txtWidgetDay, DateHelper.formatDay(now))
        rv.setOnClickPendingIntent(R.id.txtWidgetDate, WidgetHelper.createCalendarOpenIntent(ctx))
    }*/

    private fun setupDailyAlarm(ctx: Context) {
        //alarmManager.setRepeating(AlarmManager.RTC, DateHelper.getClosestMidnightMillis(), DateHelper.MILLIS_IN_DAY, getNewDayPendingIntent(ctx))
    }

    companion object {
        private const val INTENT_ACTION_NEW_DAY = "NEW_DAY_STARTED"
    }
}
