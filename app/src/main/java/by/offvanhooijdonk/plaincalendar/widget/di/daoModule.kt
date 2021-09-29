package by.offvanhooijdonk.plaincalendar.widget.di

import by.offvanhooijdonk.plaincalendar.widget.data.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val daoModule = module {
    single { AppDatabase.buildDatabase(androidApplication()) }
    single { get<AppDatabase>().widgetDao() }
}
