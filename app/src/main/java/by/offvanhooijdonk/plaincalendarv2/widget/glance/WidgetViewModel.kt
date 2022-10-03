package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel

class WidgetViewModel(
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
            android.Manifest.permission.READ_CALENDAR
        ) == PackageManager.PERMISSION_GRANTED
}
