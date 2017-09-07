package by.yahorfralou.plaincalendar.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarWidgetProvider extends AppWidgetProvider {

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
