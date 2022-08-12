package by.offvanhooijdonk.plaincalendarv2.widget.di

import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val daoModule = module {
    single { CalendarDataSource(androidContext()) }
}
