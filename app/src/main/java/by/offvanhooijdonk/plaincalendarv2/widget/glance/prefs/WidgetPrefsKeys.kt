package by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs

import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel

object WidgetPrefsKeys {
    private const val KEY_DAYS = "key_days"
    private const val KEY_BACK_COLOR = "key_background_color"
    private const val KEY_BACK_OPACITY = "key_back_opacity"
    private const val KEY_CALENDARS_IDS = "key_calendars_ids"

    private val days = intPreferencesKey(KEY_DAYS)
    private val backgroundColor = longPreferencesKey(KEY_BACK_COLOR)
    private val backgroundOpacity = floatPreferencesKey(KEY_BACK_OPACITY)
    private val calendarsIds = stringSetPreferencesKey(KEY_CALENDARS_IDS)

    fun writeToPrefs(prefs: MutablePreferences, model: WidgetModel) {
        prefs[days] = model.days
        prefs[backgroundColor] = model.backgroundColor
        prefs[backgroundOpacity] = model.opacity
        prefs[calendarsIds] = model.calendarIds.map { it.toString() }.toSet()
    }

    fun readBackgroundColor(prefs: Preferences): Color = prefs[backgroundColor]?.let { Color(it.toULong()) } ?: Color.White

    fun readBackgroundOpacity(prefs: Preferences): Float = prefs[backgroundOpacity] ?: 1.0f

    fun readCalendars(prefs: Preferences): List<Long> = prefs[calendarsIds]?.map { it.toLong() }?.toList() ?: emptyList()

    fun readDays(prefs: Preferences): Int = prefs[days] ?: 7
}
