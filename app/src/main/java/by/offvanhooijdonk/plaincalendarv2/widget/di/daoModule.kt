package by.offvanhooijdonk.plaincalendarv2.widget.di

import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import by.offvanhooijdonk.plaincalendarv2.widget.data.Prefs
import by.offvanhooijdonk.plaincalendarv2.widget.data.local.WeatherLocalStore
import by.offvanhooijdonk.plaincalendarv2.widget.data.worker.CurrentWeatherWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val daoModule = module {
    single { CalendarDataSource(androidContext()) }
    single { Prefs(androidContext()) }
    single { WeatherLocalStore(androidContext()) }

    worker { CurrentWeatherWorker(androidContext(), get(), get(), get()) }
}
