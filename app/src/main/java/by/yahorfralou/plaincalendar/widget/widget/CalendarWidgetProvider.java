package by.yahorfralou.plaincalendar.widget.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Date;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarsRemoteService;
import by.yahorfralou.plaincalendar.widget.data.calendars.observer.EventsContentObserver;
import by.yahorfralou.plaincalendar.widget.helper.DateHelper;
import by.yahorfralou.plaincalendar.widget.helper.PermissionHelper;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarWidgetProvider extends AppWidgetProvider {
    private static final String INTENT_ACTION_NEW_DAY = "NEW_DAY_STARTED";

    private EventsContentObserver contentObserver;

    @Override
    public void onEnabled(Context ctx) {
        super.onEnabled(ctx);
        Log.i(LOGCAT, "Enabled");

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, DateHelper.getClosestMidnightMillis(), DateHelper.MILLIS_IN_DAY, getNewDayPendingIntent(ctx));
        } else {
            // TODO handle
        }
    }

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(ctx, appWidgetManager, appWidgetIds);
        Log.i(LOGCAT, "Update " + Arrays.toString(appWidgetIds));

        if (contentObserver == null && PermissionHelper.hasCalendarPermissions(ctx)) {
            contentObserver = new EventsContentObserver(new Handler(), () -> {
                int[] widgetIds = getWidgetIds(ctx);
                Log.i(LOGCAT, "Observer for events changes. Widgets: " + Arrays.toString(widgetIds));
                // TODO check for the particular widgets subscriptions ?
                appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents);
            });

            ctx.getContentResolver().registerContentObserver(CalendarContract.Events.CONTENT_URI, true, contentObserver);
        }

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(ctx, CalendarsRemoteService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.calendar_widget);

            updateDateViews(rv);

            rv.setRemoteAdapter(R.id.listEvents, intent);

            rv.setEmptyView(R.id.listEvents, R.id.emptyView);

            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }

    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        super.onReceive(ctx, intent);
        Log.d(LOGCAT, "onReceive");
        Log.d(LOGCAT, "Got " + intent.getAction() + " action");

        int[] widgetIds = getWidgetIds(ctx);

        Log.i(LOGCAT, "Widgets found: " + Arrays.toString(widgetIds));

        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()) ||
                INTENT_ACTION_NEW_DAY.equals(intent.getAction())) {

            Log.i(LOGCAT, "Applying current date on Widget");
            AppWidgetManager manager = AppWidgetManager.getInstance(ctx);

            for (int widgetId : /*globalWidgetIdSet*/ widgetIds) {
                Log.i(LOGCAT, "Widget " + widgetId);
                RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.calendar_widget);
                updateDateViews(rv);

                manager.partiallyUpdateAppWidget(widgetId, rv);
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Log.i(LOGCAT, "Deleted");
    }

    @Override
    public void onDisabled(Context ctx) {
        super.onDisabled(ctx);

        Log.i(LOGCAT, "Disabled");

        // TODO check that it is here to be unsubscribed
        if (contentObserver != null) {
            ctx.getContentResolver().unregisterContentObserver(contentObserver);
        }

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getNewDayPendingIntent(ctx));
        }
    }

    private PendingIntent getNewDayPendingIntent(Context ctx) {
        Intent intent = new Intent(ctx, CalendarWidgetProvider.class);
        intent.setAction(INTENT_ACTION_NEW_DAY);
        // replace with a method that returns a *new* one
        return PendingIntent.getBroadcast(ctx, 0, intent, 0);
    }

    private int[] getWidgetIds(Context ctx) {
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        return manager.getAppWidgetIds(new ComponentName(ctx, getClass()));
    }

    private void updateDateViews(RemoteViews rv) {
        Date now = new Date(System.currentTimeMillis());
        rv.setTextViewText(R.id.txtWidgetDate, DateHelper.formatDateOnly(now));
        rv.setTextViewText(R.id.txtWidgetDay, DateHelper.formatDay(now));
    }

}
