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

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarWidgetProvider extends AppWidgetProvider {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF = new SimpleDateFormat("d EE");

    private EventsContentObserver contentObserver;
    private Set<Integer> globalWidgetIdSet = new HashSet<>();
    private AppWidgetManager widgetManagerCached;

    @Override
    public void onEnabled(Context ctx) {
        super.onEnabled(ctx);

        Log.i(LOGCAT, "Enabled");
    }

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(ctx, appWidgetManager, appWidgetIds);
        Log.i(LOGCAT, "Update " + Arrays.toString(appWidgetIds));
        widgetManagerCached = appWidgetManager;

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

        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction()) && widgetManagerCached != null) {
            for (int widgetId : globalWidgetIdSet) {
                RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.calendar_widget);
                updateDateViews(rv);

                widgetManagerCached.partiallyUpdateAppWidget(widgetId, rv);
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
        String[] dateValues = SDF.format(new Date()).split("\\s");
        rv.setTextViewText(R.id.txtWidgetDate, dateValues[0]);
        rv.setTextViewText(R.id.txtWidgetDay, dateValues[1].toLowerCase());
    }

}
