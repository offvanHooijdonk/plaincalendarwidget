package by.yahorfralou.plaincalendar.widget.data.calendars.job;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;

import java.util.Arrays;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.helper.WidgetHelper;
import by.yahorfralou.plaincalendar.widget.widget.CalendarWidgetProvider;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarChangeJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Context ctx = getApplicationContext();
        Log.i(LOGCAT, "JOB Service#onStartJob");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
        int[] widgetIds = WidgetHelper.getWidgetIds(ctx, CalendarWidgetProvider.class);
        Log.i(LOGCAT, "Widgets to update in JobService: " + Arrays.toString(widgetIds));

        appWidgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.listEvents);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
