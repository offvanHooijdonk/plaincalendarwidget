package by.offvanhooijdonk.plaincalendarv2.widget.data

import android.content.Context
import android.content.SharedPreferences

class Prefs(ctx: Context) {
    private val sharedPreferences: SharedPreferences = ctx.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    var isIntroPassed: Boolean
        get() = sharedPreferences.getBoolean(KEY_IS_INTRO_PASSED, false)
        set(value) {
            sharedPreferences.edit().putBoolean(KEY_IS_INTRO_PASSED, value).commit()
        }

    companion object {
        private const val SHARED_PREFS = "plain_calendar"

        private const val KEY_IS_INTRO_PASSED = "KEY_IS_INTRO_PASSED"
    }
}