package by.yahorfralou.plaincalendar.widget.helper;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

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
                resId = R.drawable.widget_back_corner_s;
        }

        return resId;
    }

    public static float riseTextSizeBy(Context ctx, int resId, int delta) {
        return ctx.getResources().getDimension(resId) / ctx.getResources().getDisplayMetrics().density + delta;
    }

    public static PendingIntent createCalendarOpenIntent(Context ctx) {
        long timeNow = System.currentTimeMillis();
        Intent intentCalendar = new Intent(Intent.ACTION_VIEW);
        intentCalendar.setData(CalendarContract.CONTENT_URI.buildUpon().appendPath("time").appendPath(String.valueOf(timeNow)).build());

        return PendingIntent.getActivity(ctx, 0, intentCalendar, 0);
    }

    public static Intent createEventIntent(long eventId) {
        Intent intent = new Intent();
        intent.setData(CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(String.valueOf(eventId)).build());

        return intent;
    }

}
