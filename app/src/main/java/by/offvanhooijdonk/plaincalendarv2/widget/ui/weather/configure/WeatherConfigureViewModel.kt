@file:OptIn(FlowPreview::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.data.remote.WeatherApiService
import by.offvanhooijdonk.plaincalendarv2.widget.glance.calendar.PlainGlanceWidget
import by.offvanhooijdonk.plaincalendarv2.widget.glance.calendar.prefs.writeToPrefs
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.WeatherGlanceWidget
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.prefs.writeToPrefs
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.LocationModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.toDomain
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.FinishResult
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.tabs.StyleAction
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.toIntId
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getLanguageCode
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getLocale
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale
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
    private var widgetId: Int? = null

    init {
        viewModelScope.launch {
            searchChannel.receiveAsFlow().debounce(250L.milliseconds).collectLatest {
                searchCities(it)
            }
        }

        val loc = context.getLocale()
        val countries: List<CountryModel> = Locale.getISOCountries().map { code ->
            val name = Locale("", code).getDisplayCountry(loc)
            CountryModel(code.uppercase(), name, getCountryFlagEmoji(code.uppercase()))
        }
        val noCountry = CountryModel(CODE_UNKNOWN, context.getString(R.string.any_country), "🌐")
        _state.update { state -> state.copy(countries = listOf(noCountry) + countries.sortedBy { it.title }) }
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
                _state.update {
                    it.copy(
                        selectedLocation = intent.city,
                        isSuggestionsExpanded = false,
                        widget = it.widget.copy(location = intent.city),
                        cityInput = intent.city.title
                    )
                }
                loadWeather()
            }

            Intent.DismissSuggestions -> _state.update { it.copy(isSuggestionsExpanded = false) }
            is Intent.OnBackgroundColorPick -> _state.update { it.copy(widget = it.widget.copy(backgroundColor = intent.colorValue)) }
            is Intent.OnBackgroundOpacityPick -> _state.update { it.copy(widget = it.widget.copy(opacity = intent.opacity)) }
            Intent.OnTextBoldPick -> _state.update { it.copy(widget = it.widget.copy(textStyleBold = !it.widget.textStyleBold)) }
            is Intent.OnTextColorPick -> _state.update { it.copy(widget = it.widget.copy(textColor = intent.colorValue)) }
            is Intent.OnTextSizeDeltaPick -> _state.update { it.copy(widget = it.widget.copy(textSizeDelta = intent.textSizeDelta)) }

            Intent.CountriesListDismiss -> _state.update { it.copy(isCountriesExpanded = false) }
            Intent.CountriesListExpand -> _state.update { it.copy(isCountriesExpanded = true) }
            is Intent.CountrySelect -> _state.update {
                it.copy(
                    isCountriesExpanded = false,
                    selectedCountry = intent.country,
                    cityInput = "",
                    citySuggestions = emptyList(),
                )
            }
        }
    }

    fun setWidgetId(widgetId: Int?) {
        this.widgetId = widgetId
    }

    private suspend fun searchCities(input: String) {
        if (input.length >= MIN_SEARCH_CHARS) {
            _state.update { it.copy(isSearchProgress = true) }

            val suggestions = api.searchCity(
                query = input,
                limit = if (state.value.filteringCitiesMode) SEARCH_LIMIT_FILTER else SEARCH_LIMIT_RAW,
            ).filter { city -> // either we're not filtering, or we filter and country code's the same
                !state.value.filteringCitiesMode || city.country.uppercase() == state.value.selectedCountry.code
            }.map { city ->
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
        viewModelScope.launch {
            var updateGlanceId: GlanceId? = null
            GlanceAppWidgetManager(context).getGlanceIds(WeatherGlanceWidget::class.java).forEach { glanceId ->
                if (glanceId.toIntId() == widgetId) {
                    updateGlanceId = glanceId
                    updateAppWidgetState(context, glanceId) { prefs ->
                        _state.value.widget.writeToPrefs(prefs)
                    }
                }
            }
            updateGlanceId?.let { WeatherGlanceWidget().update(context, it) }
        }

        _finishScreen.update { FinishResult.OK }
    }

    private fun loadWeather() {
        _state.value.selectedLocation?.let { city ->
            _state.update { it.copy(isSearchProgress = true) }
            viewModelScope.launch {
                val weather = api.loadForecast(
                    lat = city.lat,
                    lon = city.lon,
                    lang = context.getLanguageCode(),
                ).toDomain()

                _state.update { it.copy(isSearchProgress = false, weather = weather) }
            }
        }
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

        data object CountriesListDismiss : Intent
        data object CountriesListExpand : Intent
        data class CountrySelect(val country: CountryModel) : Intent
    }

    data class UiState(
        val widget: WeatherWidgetModel = WeatherWidgetModel(
            // sample value so the widget preview is not empty
            location = LocationModel(
                title = "Unknown",
                countryCode = "UNK",
                state = "Empty space",
                lat = 0.0,
                lon = 0.0,
            )
        ),
        val isApplyEnabled: Boolean = true,
        val cityInput: String = "",
        val citySuggestions: List<LocationModel> = emptyList(),
        val selectedLocation: LocationModel? = null,
        val isSearchProgress: Boolean = false,
        val isSuggestionsExpanded: Boolean = false,
        val weather: WeatherModel = WeatherModel(tempValue = 20.5f), // sample value so the widget preview is not empty
        val countries: List<CountryModel> = emptyList(),
        val selectedCountry: CountryModel = CountryModel("", "", ""),
        val isCountriesExpanded: Boolean = false,
        val noCountry: CountryModel = CountryModel("", "", ""),
    ) {
        val filteringCitiesMode: Boolean = selectedCountry == noCountry
    }

    companion object {
        private const val SEARCH_LIMIT_RAW = 10
        private const val SEARCH_LIMIT_FILTER = 25
        private const val MIN_SEARCH_CHARS = 3
        private const val NAME_COUNTRY_DEFAULT = "ascii"
        private const val CODE_UNKNOWN = "UNKNOWN"
    }
}

data class CountryModel(
    val code: String,
    val title: String,
    val flag: String,
)

private fun getCountryFlagEmoji(code: String): String =
    if (code.length == 2 && code.none { !it.isLetter() }) {
        val flagOffset = 0x1F1E6 - 'A'.code
        val firstChar = Character.toChars(code[0].code + flagOffset)
        val secondChar = Character.toChars(code[1].code + flagOffset)

        String(firstChar) + String(secondChar)
    } else "🌐"


fun StyleAction.toWeatherIntent(): WeatherConfigureViewModel.Intent = when (this) {
    is StyleAction.OnBackgroundColorPick -> WeatherConfigureViewModel.Intent.OnBackgroundColorPick(colorValue)
    is StyleAction.OnBackgroundOpacityPick -> WeatherConfigureViewModel.Intent.OnBackgroundOpacityPick(opacity)
    StyleAction.OnTextBoldPick -> WeatherConfigureViewModel.Intent.OnTextBoldPick
    is StyleAction.OnTextColorPick -> WeatherConfigureViewModel.Intent.OnTextColorPick(colorValue)
    is StyleAction.OnTextSizeDeltaPick -> WeatherConfigureViewModel.Intent.OnTextSizeDeltaPick(textSizeDelta)
}
