package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import android.content.Context
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.WeatherApiService
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.toDomain
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

    fun setWidgetSettings(widget: WeatherWidgetModel) {
        _state.update { it.copy(widget = widget) }
    }

    suspend fun reload() {
        val weather = api.loadForecast(
            lat = _state.value.widget.location.lat,
            lon = _state.value.widget.location.lon,
            lang = context.getLanguageCode(),
        )

        _state.update { it.copy(weather = weather.toDomain()) }
    }

    data class State(
        val widget: WeatherWidgetModel = WeatherWidgetModel(),
        val weather: WeatherModel? = null,
    )
}
