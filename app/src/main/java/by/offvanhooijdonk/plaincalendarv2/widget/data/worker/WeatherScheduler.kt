package by.offvanhooijdonk.plaincalendarv2.widget.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WeatherScheduler(
    private val context: Context
) {

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val workManager = WorkManager.getInstance(context)

    fun scheduleAll() {

        val hourlyRequest = PeriodicWorkRequestBuilder<CurrentWeatherWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "CurrentWeatherWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            hourlyRequest,
        )
    }
}