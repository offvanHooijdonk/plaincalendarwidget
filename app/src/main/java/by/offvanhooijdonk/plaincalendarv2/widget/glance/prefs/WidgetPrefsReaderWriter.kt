package by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel

object WidgetPrefsReaderWriter {
    private const val KEY_DAYS = "key_days"
    private const val KEY_BACK_COLOR = "key_background_color"
    private const val KEY_BACK_OPACITY = "key_back_opacity"
    private const val KEY_TEXT_COLOR = "key_text_color"
    private const val KEY_TEXT_SIZE = "key_text_size"
    private const val KEY_CALENDARS_IDS = "key_calendars_ids"

    private val keyDays = intPreferencesKey(KEY_DAYS)
    private val keyBackgroundColor = longPreferencesKey(KEY_BACK_COLOR)
    private val keyBackgroundOpacity = floatPreferencesKey(KEY_BACK_OPACITY)
    private val keyTextColor = longPreferencesKey(KEY_TEXT_COLOR)
    private val keyTextSize = intPreferencesKey(KEY_TEXT_SIZE)
    private val keyCalendarsIds = stringSetPreferencesKey(KEY_CALENDARS_IDS)

    fun writeToPrefs(prefs: MutablePreferences, model: WidgetModel) {
        prefs[keyDays] = model.days
        prefs[keyBackgroundColor] = model.backgroundColor
        prefs[keyBackgroundOpacity] = model.opacity
        prefs[keyTextColor] = model.textColor
        prefs[keyTextSize] = model.textSizeDelta
        prefs[keyCalendarsIds] = model.calendarIds.map { it.toString() }.toSet()
    }

    fun readWidgetModel(prefs: Preferences) =
        with(WidgetModel.createDefault()) {
            copy(
                days = prefs[keyDays] ?: days,
                backgroundColor = prefs[keyBackgroundColor] ?: backgroundColor,
                opacity = prefs[keyBackgroundOpacity] ?: opacity,
                textColor = prefs[keyTextColor] ?: textColor,
                textSizeDelta = prefs[keyTextSize] ?: textSizeDelta,
                calendarIds = prefs[keyCalendarsIds]?.map { it.toLong() }?.toList() ?: calendarIds,
            )
        }
}
