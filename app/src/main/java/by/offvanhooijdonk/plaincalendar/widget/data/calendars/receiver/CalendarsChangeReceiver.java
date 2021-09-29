package by.offvanhooijdonk.plaincalendar.widget.data.calendars.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;

import by.offvanhooijdonk.plaincalendar.widget.R;
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper;
import by.offvanhooijdonk.plaincalendar.widget.widget.CalendarWidgetProvider;

import static by.offvanhooijdonk.plaincalendar.widget.app.App.LOGCAT;

public class CalendarsChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.i(LOGCAT, "CalendarsChangeReceiver.onReceive");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
            int[] widgetIds = WidgetHelper.getWidgetIds(ctx, CalendarWidgetProvider.class);
            Log.i(LOGCAT, "Widgets to update in BReceiver: " + Arrays.toString(widgetIds));

            appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents);
        }
    }
}
