package by.offvanhooijdonk.plaincalendarv2.widget.ui.util

import android.content.Context
import java.util.Locale


fun Context.getLanguageCode(): String = getLocale().language
fun Context.getLocale(): Locale = resources.configuration.locales[0]