package by.yahorfralou.plaincalendar.widget.helper;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

/**
 * Created by Yahor_Fralou on 9/27/2017 6:57 PM.
 */

public class WidgetHelper {

    public static int[] getWidgetIds(Context ctx, Class clazz) {
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        return manager.getAppWidgetIds(new ComponentName(ctx, clazz));
    }
}
