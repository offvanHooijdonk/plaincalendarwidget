package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.app.App.Companion.LOGCAT

class WidgetViewModel(
    private val calendarDataSource: CalendarDataSource
) {

    fun loadEvents(calendarIds: List<Long>, days: Int): List<EventModel> {
        return calendarDataSource.getEvents(calendarIds, days.toLong()).also {
            Log.d(LOGCAT, "Loaded events: ${it.size}")
        }
    }
}
