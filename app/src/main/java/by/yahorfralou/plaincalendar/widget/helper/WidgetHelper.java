package by.yahorfralou.plaincalendar.widget.helper;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import by.yahorfralou.plaincalendar.widget.R;

public class WidgetHelper {

    public static int[] getWidgetIds(Context ctx, Class clazz) {
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        return manager.getAppWidgetIds(new ComponentName(ctx, clazz));
    }

    public static void notifyWidgetsDataChanged(Context ctx, int[] widgetIds) {
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        manager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents);
    }
}
