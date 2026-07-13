package by.offvanhooijdonk.plaincalendarv2.widget.data.worker

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendarv2.widget.data.local.WeatherLocalStore
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.WeatherApiService
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.WeatherGlanceWidget
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.WeatherStateDefinition
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.prefs.readWeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.toDomain
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.toIntId
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getLanguageCode

class CurrentWeatherWorker(
    val context: Context,
    params: WorkerParameters,
    private val api: WeatherApiService,
    private val store: WeatherLocalStore,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result =
        try {
            GlanceAppWidgetManager(context).getGlanceIds(WeatherGlanceWidget::class.java).lastOrNull()?.let { glanceWidgetId ->
                val state = getAppWidgetState(context, WeatherStateDefinition, glanceWidgetId)
                state.readWeatherWidgetModel(glanceWidgetId.toIntId().toLong())?.let { widget ->
                    val data = api.loadForecast(lat = widget.location.lat, lon = widget.location.lon, lang = context.getLanguageCode())

                    store.saveCurrentWeather(data.toDomain())

                    WeatherGlanceWidget().update(context, glanceWidgetId)
                }
                Result.success()
            } ?: Result.success()
        } catch (e: Exception) {
            Log.e(App.LOGCAT, "Error loading current weather via worker!", e)
            Result.retry()
        }
}