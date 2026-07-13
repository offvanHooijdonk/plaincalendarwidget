package by.offvanhooijdonk.plaincalendarv2.widget.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.serialization.json.Json

class WeatherLocalStore(
    private val context: Context,
) {
    val Context.datastore by preferencesDataStore(name = "weather_widget_data_store")

    suspend fun saveCurrentWeather(data: WeatherModel) {
        context.datastore.edit { prefs ->
            prefs[CURRENT_DATA] = Json.encodeToString(data)
        }
    }

    suspend fun getCurrentWeather(): WeatherModel? =
        try {
            context.datastore.data.lastOrNull()?.get(CURRENT_DATA)?.let { Json.decodeFromString<WeatherModel>(it) }
        } catch (e: Exception) {
            Log.e(App.LOGCAT, "Error reading weather data from the local storage", e)
            null
        }


    companion object {
        val CURRENT_DATA = stringPreferencesKey("current_weather_json")
        val FOUR_HOUR_DATA = stringPreferencesKey("four_hour_weather_json")
    }
}