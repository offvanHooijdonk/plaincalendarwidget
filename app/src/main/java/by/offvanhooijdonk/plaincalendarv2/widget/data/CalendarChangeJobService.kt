package by.offvanhooijdonk.plaincalendarv2.widget.data

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendarv2.widget.glance.PlainGlanceWidget

class CalendarChangeJobService : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        Log.d(App.LOGCAT, "JOB Service#onStartJob")

        PlainGlanceWidget().loadData()

        scheduleCalendarChangeJob(applicationContext)

        return false
    }

    override fun onStopJob(params: JobParameters): Boolean = true

    companion object {
        private const val TRIGGER_MAX_DELAY_TIME: Long = 1000
        private const val JOB_CALENDAR_CHANGE_ID = 1002

        fun scheduleCalendarChangeJob(ctx: Context) {
            Log.d(App.LOGCAT, "Creating Job Schedule.")

            val uriTrigger: Uri = CalendarDataSource.makeEventsObservationUri()
            val builder = JobInfo.Builder(JOB_CALENDAR_CHANGE_ID, ComponentName(ctx, CalendarChangeJobService::class.java))
                .addTriggerContentUri(JobInfo.TriggerContentUri(uriTrigger, JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                .setTriggerContentMaxDelay(TRIGGER_MAX_DELAY_TIME)
            ctx.getSystemService(JobScheduler::class.java)?.schedule(builder.build())
        }

        fun cancelCalendarChangeJob(ctx: Context) {
            ctx.getSystemService(JobScheduler::class.java).cancelAll()
        }
    }
}
