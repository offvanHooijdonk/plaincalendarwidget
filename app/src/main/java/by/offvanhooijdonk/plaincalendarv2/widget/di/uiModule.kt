package by.offvanhooijdonk.plaincalendarv2.widget.di

import by.offvanhooijdonk.plaincalendarv2.widget.glance.calendar.WidgetViewModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.ConfigureViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ConfigureViewModel(androidContext(), get(), get()) }
    factory { WidgetViewModel(androidContext(), get()) }
}
