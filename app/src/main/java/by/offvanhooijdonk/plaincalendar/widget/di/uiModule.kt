package by.offvanhooijdonk.plaincalendar.widget.di

import by.offvanhooijdonk.plaincalendar.widget.ui.configure.ConfigureViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { ConfigureViewModel(get()) }
}
