package by.yahorfralou.plaincalendar.widget.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.Date;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarsRemoteService;
import by.yahorfralou.plaincalendar.widget.data.calendars.job.CalendarChangeJobService;
import by.yahorfralou.plaincalendar.widget.data.calendars.observer.EventsContentObserver;
import by.yahorfralou.plaincalendar.widget.helper.DateHelper;
import by.yahorfralou.plaincalendar.widget.helper.WidgetHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarWidgetProvider extends AppWidgetProvider {
    private static final String INTENT_ACTION_NEW_DAY = "NEW_DAY_STARTED";

    private EventsContentObserver contentObserver;

    @Override
    public void onEnabled(Context ctx) {
        super.onEnabled(ctx);
        Log.i(LOGCAT, "Enabled");

        setupDailyAlarm(ctx);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            CalendarChangeJobService.scheduleCalendarChangeJob(ctx);
        }
    }

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(ctx, appWidgetManager, appWidgetIds);
        Log.i(LOGCAT, "Update " + Arrays.toString(appWidgetIds));

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(ctx, CalendarsRemoteService.class);
            // Add the app widget ID to the intent extras.
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.widget_calendars);

            PlainCalendarWidgetApp.getAppDatabase().widgetDao().getById(appWidgetId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(widgetBean -> {
                        Log.i(LOGCAT, "onUpdate. Corners " + widgetBean.getCorners());
                        int backResId = WidgetHelper.getBackgroundRes(widgetBean.getCorners());
                        rv.setImageViewResource(R.id.widgetBack, backResId);
                        rv.setInt(R.id.widgetBack, "setColorFilter", widgetBean.getBackgroundColor());
                        rv.setInt(R.id.widgetBack, "setImageAlpha", (widgetBean.getOpacity() * 0xFF) / 100);

                        rv.setTextColor(R.id.txtWidgetDate, widgetBean.getTextColor());
                        rv.setTextColor(R.id.txtWidgetDay, widgetBean.getTextColor());
                        rv.setInt(R.id.dividerDate, "setColorFilter", widgetBean.getTextColor());
                        rv.setTextColor(R.id.emptyView, widgetBean.getTextColor());
                        rv.setTextViewTextSize(R.id.emptyView, TypedValue.COMPLEX_UNIT_SP, WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_event_title, widgetBean.getTextSizeDelta()));

                        updateDateViews(ctx, rv);
                        rv.setRemoteAdapter(R.id.listEvents, intent);
                        rv.setEmptyView(R.id.listEvents, R.id.emptyView);

                        Intent intentTemplate = new Intent(Intent.ACTION_VIEW);
                        rv.setPendingIntentTemplate(R.id.listEvents, PendingIntent.getActivity(ctx, 0, intentTemplate, 0));

                        appWidgetManager.updateAppWidget(appWidgetId, rv);
                    }, th -> {
                    });
        }

    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        super.onReceive(ctx, intent);
        Log.d(LOGCAT, "onReceive");
        Log.d(LOGCAT, "Got " + intent.getAction() + " action");

        if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIME_CHANGED.equals(intent.getAction()) ||
                Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction()) ||
                INTENT_ACTION_NEW_DAY.equals(intent.getAction()) ||
                Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            int[] widgetIds = WidgetHelper.getWidgetIds(ctx, CalendarWidgetProvider.class);

            Log.i(LOGCAT, "Applying current date on Widgets");

            // TODO send update Broadcast instead?
            WidgetHelper.notifyWidgetsDataChanged(ctx, widgetIds);

            AppWidgetManager manager = AppWidgetManager.getInstance(ctx);

            for (int widgetId : widgetIds) {
                Log.i(LOGCAT, "Widget " + widgetId);
                RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.widget_calendars);
                // TODO update views basing on WidgetBean
                updateDateViews(ctx, rv);

                manager.partiallyUpdateAppWidget(widgetId, rv);
            }

            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                setupDailyAlarm(ctx);
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

    private void updateDateViews(Context ctx, RemoteViews rv) {
        Date now = new Date(System.currentTimeMillis());
        rv.setTextViewText(R.id.txtWidgetDate, DateHelper.formatDateOnly(now));
        rv.setTextViewText(R.id.txtWidgetDay, DateHelper.formatDay(now));

        rv.setOnClickPendingIntent(R.id.txtWidgetDate, WidgetHelper.createCalendarOpenIntent(ctx));
    }

    private void setupDailyAlarm(Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC, DateHelper.getClosestMidnightMillis(), DateHelper.MILLIS_IN_DAY, getNewDayPendingIntent(ctx));
        } else {
            // TODO handle
        }
    }
}
