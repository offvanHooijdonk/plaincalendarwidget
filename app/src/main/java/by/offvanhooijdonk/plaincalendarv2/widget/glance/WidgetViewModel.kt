package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.CalendarDataSource
import by.offvanhooijdonk.plaincalendar.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.app.App.Companion.LOGCAT

class WidgetViewModel(
    private val calendarDataSource: CalendarDataSource
) {
    private val _eventLiveData = MutableLiveData(emptyList<EventModel>())
    val eventLiveData: LiveData<List<EventModel>> = _eventLiveData

    fun loadEvent(calendarIds: List<Long>, days: Int): List<EventModel> {
        /*_eventLiveData.value =*/return calendarDataSource.getEvents(calendarIds, days).also {
            Log.d(LOGCAT, "Loaded events: ${it.size}")
        }
    }
}
