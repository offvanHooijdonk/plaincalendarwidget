package by.offvanhooijdonk.plaincalendar.widget.data.calendars.observer

import android.database.ContentObserver
import android.os.Handler
import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.app.App

class EventsContentObserver(handler: Handler, private val onDataChanged: () -> Unit) : ContentObserver(handler) {

    init {
        Log.i(App.LOGCAT, "Content Observer created")
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        Log.d(App.LOGCAT, "onChange")
        onDataChanged()
    }

    override fun deliverSelfNotifications(): Boolean {
        return true
    }
}
