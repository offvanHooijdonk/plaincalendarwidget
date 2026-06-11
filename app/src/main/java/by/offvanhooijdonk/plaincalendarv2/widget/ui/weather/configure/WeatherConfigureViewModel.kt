package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure

import androidx.lifecycle.ViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.FinishResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WeatherConfigureViewModel : ViewModel() {
    private val _finishScreen = MutableStateFlow<FinishResult?>(null)
    val finishScreen: StateFlow<FinishResult?> = _finishScreen

    fun save() {
        _finishScreen.update { FinishResult.OK }
    }
}