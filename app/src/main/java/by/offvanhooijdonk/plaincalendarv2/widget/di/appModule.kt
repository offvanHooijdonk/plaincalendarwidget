package by.offvanhooijdonk.plaincalendarv2.widget.di

import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    single { AppWidgetManager.getInstance(androidContext()) }
}
