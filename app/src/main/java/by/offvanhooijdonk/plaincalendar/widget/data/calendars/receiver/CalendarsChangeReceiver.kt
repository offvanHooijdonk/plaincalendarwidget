package by.offvanhooijdonk.plaincalendar.widget.data.calendars.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.app.App

class CalendarsChangeReceiver : BroadcastReceiver() { // todo remove ?
    override fun onReceive(ctx: Context, intent: Intent) {
        Log.i(App.LOGCAT, "CalendarsChangeReceiver.onReceive")
    }
}
