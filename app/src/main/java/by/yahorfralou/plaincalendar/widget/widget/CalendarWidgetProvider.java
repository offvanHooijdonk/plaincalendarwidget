package by.yahorfralou.plaincalendar.widget.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarsRemoteService;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarWidgetProvider extends AppWidgetProvider {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF = new SimpleDateFormat("d EE");

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        //context.startActivity(new Intent(context, SettingsActivity.class));
        Log.i(LOGCAT, "Enabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(LOGCAT, "Update");

        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, CalendarsRemoteService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.calendar_widget);

            String[] dateValues = SDF.format(new Date()).split("\\s");
            rv.setTextViewText(R.id.txtWidgetDate, dateValues[0]);
            rv.setTextViewText(R.id.txtWidgetDay, dateValues[1].toLowerCase());

            rv.setRemoteAdapter(R.id.listEvents, intent);

            rv.setEmptyView(R.id.listEvents, R.id.emptyView);

            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);

        Log.i(LOGCAT, "Deleted");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Log.i(LOGCAT, "Disabled");
    }
}
