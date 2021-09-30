package by.offvanhooijdonk.plaincalendar.widget.data.calendars.job

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import by.offvanhooijdonk.plaincalendar.widget.app.App
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.CalendarDataSource
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper
import by.offvanhooijdonk.plaincalendar.widget.widget.CalendarWidgetProvider
import org.koin.android.ext.android.inject

class CalendarChangeJobService : JobService() {
    private val widgetHelper: WidgetHelper by inject()

    override fun onStartJob(params: JobParameters): Boolean {
        val ctx: Context = applicationContext
        Log.d(App.LOGCAT, "JOB Service#onStartJob")
        val widgetIds = widgetHelper.getWidgetIds(CalendarWidgetProvider::class.java)
        Log.d(App.LOGCAT, "Widgets to update in JobService: " + widgetIds.contentToString())
        widgetHelper.notifyWidgetsDataChanged(widgetIds)
        if (widgetIds.isNotEmpty()) {
            scheduleCalendarChangeJob(ctx)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    companion object {
        private const val TRIGGER_MAX_DELAY_TIME: Long = 1000
        private const val JOB_CALENDAR_CHANGE_ID = 1002

        fun scheduleCalendarChangeJob(ctx: Context) {
            Log.i(App.LOGCAT, "Creating Job Schedule.")

            val uriTrigger: Uri = CalendarDataSource.makeEventsObservationUri()
            val builder = JobInfo.Builder(JOB_CALENDAR_CHANGE_ID, ComponentName(ctx, CalendarChangeJobService::class.java))
                .addTriggerContentUri(JobInfo.TriggerContentUri(uriTrigger, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                .setTriggerContentMaxDelay(TRIGGER_MAX_DELAY_TIME)
            val jobScheduler = ctx.getSystemService(JobScheduler::class.java)
            if (jobScheduler != null) {
                jobScheduler.schedule(builder.build())
            } else {
                Log.e(App.LOGCAT, "Job Scheduler received from ctx is null!")
            }
        }
    }
}
