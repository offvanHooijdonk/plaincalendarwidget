package by.offvanhooijdonk.plaincalendar.widget.di

import by.offvanhooijdonk.plaincalendar.widget.helper.PrefHelper
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val helperModule = module {
    single { WidgetHelper(androidContext(), get()) }
    single { PrefHelper(androidContext()) }
}
