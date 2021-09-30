package by.offvanhooijdonk.plaincalendar.widget.di

import android.app.AlarmManager
import android.appwidget.AppWidgetManager
import android.content.Context
import by.offvanhooijdonk.plaincalendar.widget.widget.EventsRemoteViewsFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    single { AppWidgetManager.getInstance(androidContext()) }

    factory { params ->
        EventsRemoteViewsFactory(
            androidContext(),
            get(),
            get(),
            params[0]
        )
    }
}
