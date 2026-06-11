package by.offvanhooijdonk.plaincalendarv2.widget.di

import by.offvanhooijdonk.plaincalendarv2.widget.glance.calendar.CalendarWidgetViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.weather.WeatherWidgetViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.ConfigureViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.weather.configure.WeatherConfigureViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ConfigureViewModel(androidContext(), get(), get()) }
    viewModel { WeatherConfigureViewModel() }
    factory { CalendarWidgetViewModel(androidContext(), get()) }
    factory { WeatherWidgetViewModel() }
}
