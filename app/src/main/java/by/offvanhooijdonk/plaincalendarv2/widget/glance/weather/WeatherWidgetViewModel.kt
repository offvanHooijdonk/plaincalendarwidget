package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WeatherWidgetViewModel : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    fun reload() {
        _state.update { it.copy(cityName = "", temp = 18.6f) }
    }

    data class State(
        val cityName: String = "",
        val temp: Float = 0.0f,
    )
}