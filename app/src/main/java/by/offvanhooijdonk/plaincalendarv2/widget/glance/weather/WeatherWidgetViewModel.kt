package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.content.Context
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.WeatherApiService
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getLanguageCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WeatherWidgetViewModel(
    private val context: Context,
    private val api: WeatherApiService,
) {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    suspend fun reload() {
        val weather = api.loadForecast(
            lat = 52.4238936,
            lon = 31.0131698,
            lang = context.getLanguageCode(),
        )

        _state.update { it.copy(cityName = weather.name, temp = weather.main.temp) }
    }

    data class State(
        val cityName: String = "",
        val temp: Float = 0.0f,
    )
}
