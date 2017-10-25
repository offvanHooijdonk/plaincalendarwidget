package by.yahorfralou.plaincalendar.widget.helper;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

public class WidgetHelper {

    public static int[] getWidgetIds(Context ctx, Class clazz) {
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        return manager.getAppWidgetIds(new ComponentName(ctx, clazz));
    }

    public static void notifyWidgetsDataChanged(Context ctx, int[] widgetIds) {
        AppWidgetManager manager = AppWidgetManager.getInstance(ctx);
        manager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents);
    }

    public static int getBackgroundRes(WidgetBean.Corners corners) {
        int resId;
        switch (corners) {
            case NO_CORNER:
                resId = R.drawable.widget_back_no_corner;
                break;
            case SMALL:
                resId = R.drawable.widget_back_corner_s;
                break;
            case MEDIUM:
                resId = R.drawable.widget_back_corner_m;
                break;
            case LARGE:
                resId = R.drawable.widget_back_corner_l;
                break;
            case XLARGE:
                resId = R.drawable.widget_back_corner_xl;
                break;
            default:
                resId = R.drawable.widget_back_corner_m;
        }

        return resId;
    }

    public static float riseTextSizeBy(Context ctx, int resId, int delta) {
        return ctx.getResources().getDimension(resId) / ctx.getResources().getDisplayMetrics().density + delta;
    }
}
