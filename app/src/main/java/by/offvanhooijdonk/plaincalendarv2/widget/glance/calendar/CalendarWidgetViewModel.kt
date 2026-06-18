package by.offvanhooijdonk.plaincalendarv2.widget.glance.calendar

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.EventModel

class CalendarWidgetViewModel(
    private val context: Context,
    private val calendarDataSource: CalendarDataSource
) {

    fun loadEvents(calendarIds: List<Long>, days: Int): List<EventModel> =
        if (checkCalendarPermission()) {
            calendarDataSource.getEvents(calendarIds, days.toLong())
        } else {
            emptyList()
        }

    private fun checkCalendarPermission() =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
}
