package by.offvanhooijdonk.plaincalendarv2.widget.glance

import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel

object WidgetPrefsKeys {
    private const val KEY_BACK_COLOR = "key_background_color"
    private const val KEY_BACK_OPACITY = "key_back_opacity"

    private val backgroundColor = longPreferencesKey(KEY_BACK_COLOR)
    private val backgroundOpacity = floatPreferencesKey(KEY_BACK_OPACITY)

    fun writeToPrefs(prefs: MutablePreferences, model: WidgetModel) {
        prefs[backgroundColor] = model.backgroundColor
        prefs[backgroundOpacity] = model.opacity
    }

    fun readBackgroundColor(prefs: Preferences): Color = prefs[backgroundColor]?.let { Color(it.toULong()) } ?: Color.White

    fun readBackgroundOpacity(prefs: Preferences): Float = prefs[backgroundOpacity] ?: 1.0f
}
