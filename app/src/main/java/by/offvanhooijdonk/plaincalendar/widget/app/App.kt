package by.offvanhooijdonk.plaincalendar.widget.app

import android.app.Application
import android.util.Log
import by.offvanhooijdonk.plaincalendar.widget.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules()
        }

        appDatabase = AppDatabase.buildDatabase(applicationContext)
    }

    companion object {
        const val LOGCAT = "=== PCW ==="
        var appDatabase: AppDatabase? = null
            private set
    }
}
