package by.yahorfralou.plaincalendar.widget.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarsRemoteService;
import by.yahorfralou.plaincalendar.widget.data.calendars.observer.EventsContentObserver;
import by.yahorfralou.plaincalendar.widget.helper.DateHelper;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarWidgetProvider extends AppWidgetProvider {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF = new SimpleDateFormat("d EE");

    private EventsContentObserver contentObserver;
    private static Set<Integer> globalWidgetIdSet = new HashSet<>();

    @Override
    public void onEnabled(Context ctx) {
        super.onEnabled(ctx);
        Log.i(LOGCAT, "Enabled");
    }

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(ctx, appWidgetManager, appWidgetIds);
        Log.i(LOGCAT, "Update " + Arrays.toString(appWidgetIds));

        for (int appWidgetId : appWidgetIds) {
            globalWidgetIdSet.add(appWidgetId);
        }

        if (contentObserver == null) {
            contentObserver = new EventsContentObserver(new Handler(), () -> {
                for (Integer wId : globalWidgetIdSet) {
                    appWidgetManager.notifyAppWidgetViewDataChanged(wId, R.id.listEvents);
                }
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
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);

        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {

            Log.i(LOGCAT, "Applying current date on Event");

            for (int widgetId : globalWidgetIdSet) {
                Log.i(LOGCAT, "Event " + widgetId);
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

        for (int widgetId : appWidgetIds) {
            globalWidgetIdSet.remove(widgetId);
        }
    }

    @Override
    public void onDisabled(Context ctx) {
        super.onDisabled(ctx);

        Log.i(LOGCAT, "Disabled");

        if (contentObserver != null) {
            ctx.getContentResolver().unregisterContentObserver(contentObserver);
        }
    }

    private void updateDateViews(RemoteViews rv) {
        Date now = new Date(System.currentTimeMillis());
        rv.setTextViewText(R.id.txtWidgetDate, DateHelper.formatDateOnly(now));
        rv.setTextViewText(R.id.txtWidgetDay, DateHelper.formatDay(now));
    }

}
