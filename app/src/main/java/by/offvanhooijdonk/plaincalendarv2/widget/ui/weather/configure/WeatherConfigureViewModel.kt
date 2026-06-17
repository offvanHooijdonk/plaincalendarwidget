@file:OptIn(FlowPreview::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.WeatherApiService
import by.offvanhooijdonk.plaincalendarv2.widget.model.WeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.FinishResult
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.tabs.StyleAction
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getLanguageCode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class WeatherConfigureViewModel(
    private val context: Context,
    private val api: WeatherApiService,
) : ViewModel() {
    private val _finishScreen = MutableStateFlow<FinishResult?>(null)
    val finishScreen: StateFlow<FinishResult?> = _finishScreen

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val searchChannel = Channel<String>()

    init {
        viewModelScope.launch {
            searchChannel.receiveAsFlow().debounce(250L.milliseconds).collectLatest {
                searchCities(it)
            }
        }
    }


    fun onIntent(intent: Intent) {
        when (intent) {
            Intent.ApplyClick -> save()
            is Intent.CityInput -> {
                _state.update { it.copy(cityInput = intent.input) }
                viewModelScope.launch {
                    searchChannel.send(intent.input)
                }
            }
            is Intent.CitySelect -> {
                _state.update { it.copy(selectedLocation = intent.city) }
            }

            Intent.DismissSuggestions -> _state.update { it.copy(isSuggestionsExpanded = false) }
            is Intent.OnBackgroundColorPick -> _state.update { it.copy(widget = it.widget.copy(backgroundColor = intent.colorValue)) }
            is Intent.OnBackgroundOpacityPick -> _state.update { it.copy(widget = it.widget.copy(opacity = intent.opacity)) }
            Intent.OnTextBoldPick -> _state.update { it.copy(widget = it.widget.copy(textStyleBold = !it.widget.textStyleBold)) }
            is Intent.OnTextColorPick -> _state.update { it.copy(widget = it.widget.copy(textColor = intent.colorValue)) }
            is Intent.OnTextSizeDeltaPick -> _state.update { it.copy(widget = it.widget.copy(textSizeDelta = intent.textSizeDelta)) }
        }
    }

    private suspend fun searchCities(input: String) {
        if (input.length >= 2) {
            _state.update { it.copy(isSearchProgress = true) }

            val suggestions = api.searchCity(
                query = input,
                limit = SEARCH_LIMIT,
            ).map { city ->
                LocationModel(
                    title = city.localNames?.let { it[context.getLanguageCode()] ?: it[NAME_COUNTRY_DEFAULT] } ?: city.name,
                    countryCode = city.country,
                    state = city.state,
                    lat = city.lat,
                    lon = city.lon,
                )
            }

            _state.update { it.copy(isSearchProgress = false, citySuggestions = suggestions, isSuggestionsExpanded = true) }
        } else {
            _state.update { it.copy(isSearchProgress = false, citySuggestions = emptyList(), isSuggestionsExpanded = false) }
        }
    }

    private fun save() {
        _finishScreen.update { FinishResult.OK }
    }

    sealed interface Intent {
        data object ApplyClick : Intent
        data class CityInput(val input: String) : Intent
        data class CitySelect(val city: LocationModel) : Intent
        data object DismissSuggestions : Intent

        data class OnBackgroundColorPick(val colorValue: Long) : Intent
        data class OnBackgroundOpacityPick(val opacity: Float) : Intent
        data class OnTextColorPick(val colorValue: Long) : Intent
        data class OnTextSizeDeltaPick(val textSizeDelta: Int) : Intent
        data object OnTextBoldPick : Intent
    }

    data class UiState(
        val widget: WeatherWidgetModel = WeatherWidgetModel(),
        val isApplyEnabled: Boolean = true,
        val cityInput: String = "",
        val citySuggestions: List<LocationModel> = emptyList(),
        val selectedLocation: LocationModel? = null,
        val isSearchProgress: Boolean = false,
        val isSuggestionsExpanded: Boolean = false,
    )

    companion object {
        private const val SEARCH_LIMIT = 5
        private const val NAME_COUNTRY_DEFAULT = "ascii"
    }
}

data class LocationModel(
    val title: String,
    val countryCode: String,
    val state: String,
    val lat: Double,
    val lon: Double,
)

fun StyleAction.toWeatherIntent(): WeatherConfigureViewModel.Intent = when (this) {
    is StyleAction.OnBackgroundColorPick -> WeatherConfigureViewModel.Intent.OnBackgroundColorPick(colorValue)
    is StyleAction.OnBackgroundOpacityPick -> WeatherConfigureViewModel.Intent.OnBackgroundOpacityPick(opacity)
    StyleAction.OnTextBoldPick -> WeatherConfigureViewModel.Intent.OnTextBoldPick
    is StyleAction.OnTextColorPick -> WeatherConfigureViewModel.Intent.OnTextColorPick(colorValue)
    is StyleAction.OnTextSizeDeltaPick -> WeatherConfigureViewModel.Intent.OnTextSizeDeltaPick(textSizeDelta)
}