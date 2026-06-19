package by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.prefs

import androidx.datastore.preferences.core.*
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.LocationModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.weather.WeatherWidgetModel

private const val KEY_BACK_COLOR = "key_background_color"
private const val KEY_BACK_OPACITY = "key_back_opacity"
private const val KEY_TEXT_COLOR = "key_text_color"
private const val KEY_TEXT_SIZE = "key_text_size"
private const val KEY_TEXT_BOLD = "key_text_bold"
private const val KEY_LOCATION_TITLE = "KEY_LOCATION_TITLE"
private const val KEY_LOCATION_STATE = "KEY_LOCATION_STATE"
private const val KEY_LOCATION_COUNTRY = "KEY_LOCATION_COUNTRY"
private const val KEY_LOCATION_LAT = "KEY_LOCATION_LAT"
private const val KEY_LOCATION_LON = "KEY_LOCATION_LON"

private val keyBackgroundColor = longPreferencesKey(KEY_BACK_COLOR)
private val keyBackgroundOpacity = floatPreferencesKey(KEY_BACK_OPACITY)
private val keyTextColor = longPreferencesKey(KEY_TEXT_COLOR)
private val keyTextSize = intPreferencesKey(KEY_TEXT_SIZE)
private val keyTextBold = booleanPreferencesKey(KEY_TEXT_BOLD)
private val keyLocationTitle = stringPreferencesKey(KEY_LOCATION_TITLE)
private val keyLocationState = stringPreferencesKey(KEY_LOCATION_STATE)
private val keyLocationCountry = stringPreferencesKey(KEY_LOCATION_COUNTRY)
private val keyLocationLat = doublePreferencesKey(KEY_LOCATION_LAT)
private val keyLocationLon = doublePreferencesKey(KEY_LOCATION_LON)

fun WeatherWidgetModel.writeToPrefs(prefs: MutablePreferences) {
    prefs[keyBackgroundColor] = backgroundColor
    prefs[keyBackgroundOpacity] = opacity
    prefs[keyTextColor] = textColor
    prefs[keyTextBold] = textStyleBold
    prefs[keyTextSize] = textSizeDelta
    prefs[keyLocationTitle] = location.title
    prefs[keyLocationState] = location.state ?: ""
    prefs[keyLocationCountry] = location.countryCode
    prefs[keyLocationLat] = location.lat
    prefs[keyLocationLon] = location.lon

}

fun Preferences.readWeatherWidgetModel(glanceId: Long? = null): WeatherWidgetModel? =
    get(keyLocationTitle)?.let { // return only if there is a saved model
        with(WeatherWidgetModel()) {
            copy(
                id = glanceId ?: id,
                backgroundColor = get(keyBackgroundColor) ?: backgroundColor,
                opacity = get(keyBackgroundOpacity) ?: opacity,
                textColor = get(keyTextColor) ?: textColor,
                textSizeDelta = get(keyTextSize) ?: textSizeDelta,
                textStyleBold = get(keyTextBold) ?: textStyleBold,
                location = LocationModel(
                    title = get(keyLocationTitle) ?: location.title,
                    countryCode = get(keyLocationCountry) ?: location.countryCode,
                    state = get(keyLocationState) ?: location.state,
                    lat = get(keyLocationLat) ?: location.lat,
                    lon = get(keyLocationLon) ?: location.lon,
                )
            )
        }
    }
