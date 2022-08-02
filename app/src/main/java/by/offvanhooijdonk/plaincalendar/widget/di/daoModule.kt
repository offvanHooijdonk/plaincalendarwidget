package by.offvanhooijdonk.plaincalendar.widget.di

import by.offvanhooijdonk.plaincalendar.widget.data.calendars.CalendarDataSource
import by.offvanhooijdonk.plaincalendar.widget.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val daoModule = module {
    /*single { AppDatabase.buildDatabase(androidContext()) }
    single { get<AppDatabase>().widgetDao() }
    single { get<AppDatabase>().calendarDao() }*/
    single { CalendarDataSource(androidContext()) }
}
