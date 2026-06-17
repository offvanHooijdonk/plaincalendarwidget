package by.offvanhooijdonk.plaincalendarv2.widget.ui.util

import android.content.Context


fun Context.getLanguageCode(): String = resources.configuration.locales[0].language