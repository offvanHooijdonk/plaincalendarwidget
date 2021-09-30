package by.offvanhooijdonk.plaincalendar.widget.data.calendars

import android.content.Intent
import android.util.Log
import android.widget.RemoteViewsService
import by.offvanhooijdonk.plaincalendar.widget.app.App
import org.koin.android.ext.android.get
import org.koin.core.parameter.parametersOf

class CalendarsRemoteService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        Log.d(App.LOGCAT, "RemoteService::onGetViewFactory")
        return get(parameters = { parametersOf(intent) })
    }
}
