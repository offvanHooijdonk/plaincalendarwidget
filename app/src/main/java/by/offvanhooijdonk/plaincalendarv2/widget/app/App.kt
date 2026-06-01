package by.offvanhooijdonk.plaincalendarv2.widget.app

import android.app.Application
import by.offvanhooijdonk.plaincalendarv2.widget.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(allModules)
        }
    }

    companion object {
        const val LOGCAT = "PLNCLDWDG"
    }
}
