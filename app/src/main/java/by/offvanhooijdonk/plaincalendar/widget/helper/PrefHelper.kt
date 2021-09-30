package by.offvanhooijdonk.plaincalendar.widget.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import by.offvanhooijdonk.plaincalendar.widget.R

class PrefHelper(private val ctx: Context) { // todo convert to DataStore
    private val prefs: SharedPreferences = getSharedPreference()

    fun getDefaultOpacityPerCent(): Int {
        return prefs.getInt(PREF_OPACITY_PER_CENT, DEFAULT_OPACITY_PERCENT)
    }

    fun setDefaultOpacityPerCent(ctx: Context, opacity: Int) {
        prefs.edit().putInt(PREF_OPACITY_PER_CENT, opacity).apply()
    }

    fun getDefaultBackColor(ctx: Context): Int {
        return prefs.getInt(PREF_DEFAULT_BACK_COLOR, ctx.resources.getColor(DEFAULT_BACK_COLOR_RESOURCE))
    }

    fun setDefaultBackColor(ctx: Context, color: Int) {
        prefs.edit().putInt(PREF_DEFAULT_BACK_COLOR, color).apply()
    }

    fun getDefaultTextColor(ctx: Context): Int {
        return prefs.getInt(PREF_DEFAULT_TEXT_COLOR, ctx.resources.getColor(DEFAULT_TEXT_COLOR_RESOURCE))
    }

    fun setDefaultTextColor(ctx: Context, color: Int) {
        prefs.edit().putInt(PREF_DEFAULT_TEXT_COLOR, color).apply()
    }

    private fun getSharedPreference(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    companion object {
        private const val PREF_OPACITY_PER_CENT = "pref_opacity_per_cent"
        private const val PREF_DEFAULT_BACK_COLOR = "pref_default_back_color"
        private const val PREF_DEFAULT_TEXT_COLOR = "pref_default_text_color"
        private const val DEFAULT_OPACITY_PERCENT = 80
        private const val DEFAULT_BACK_COLOR_RESOURCE = R.color.widget_default_back
        private const val DEFAULT_TEXT_COLOR_RESOURCE = R.color.widget_default_text
    }
}
