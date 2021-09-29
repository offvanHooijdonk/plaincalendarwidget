package by.offvanhooijdonk.plaincalendar.widget.data.calendars.job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.Arrays;

import by.offvanhooijdonk.plaincalendar.widget.data.calendars.CalendarDataSource;
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper;
import by.offvanhooijdonk.plaincalendar.widget.widget.CalendarWidgetProvider;

import static by.offvanhooijdonk.plaincalendar.widget.app.App.LOGCAT;

@TargetApi(Build.VERSION_CODES.N)
public class CalendarChangeJobService extends JobService {
    private static final long TRIGGER_MAX_DELAY_TIME = 1000;
    private static final int JOB_CALENDAR_CHANGE_ID = 1002;

    @Override
    public boolean onStartJob(JobParameters params) {
        Context ctx = getApplicationContext();
        Log.i(LOGCAT, "JOB Service#onStartJob");

        int[] widgetIds = WidgetHelper.getWidgetIds(ctx, CalendarWidgetProvider.class);
        Log.i(LOGCAT, "Widgets to update in JobService: " + Arrays.toString(widgetIds));

        WidgetHelper.notifyWidgetsDataChanged(ctx, widgetIds);

        if (widgetIds != null && widgetIds.length > 0) {
            scheduleCalendarChangeJob(ctx);
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void scheduleCalendarChangeJob(Context ctx) {
        Log.i(LOGCAT, "Creating Job Schedule.");
        Uri uriTrigger = CalendarDataSource.makeEventsObservationUri();

        JobInfo.Builder builder = new JobInfo.Builder(JOB_CALENDAR_CHANGE_ID, new ComponentName(ctx, CalendarChangeJobService.class))
                .addTriggerContentUri(new JobInfo.TriggerContentUri(uriTrigger, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                .setTriggerContentMaxDelay(TRIGGER_MAX_DELAY_TIME);

        JobScheduler jobScheduler = ctx.getSystemService(JobScheduler.class);
        if (jobScheduler != null) {
            jobScheduler.schedule(builder.build());
        } else {
            Log.e(LOGCAT, "Job Scheduler received from ctx is null!");
        }
    }
}
