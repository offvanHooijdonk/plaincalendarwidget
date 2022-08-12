package by.offvanhooijdonk.plaincalendarv2.widget.data

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.ext.millis
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toMidnightAtDay
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class CalendarDataSource(private val ctx: Context) {

    fun loadCalendars(): List<CalendarModel> {
        val calendarList: MutableList<CalendarModel> = mutableListOf()
        try {
            prepareCalendarsCursor(ctx.contentResolver, CalendarContract.Calendars.CONTENT_URI)?.use { cur ->
                while (cur.moveToNext()) {
                    val calendar = CalendarModel(
                        // todo check for nulls
                        id = cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID)),
                        displayName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)),
                        accountName = cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)),
                        color = cur.getInt(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR)),
                        isPrimaryOnAccount = cur.getInt(cur.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)) == BOOLEAN_TRUE,
                    )
                    calendarList.add(calendar)
                }
            }
        } catch (e: SecurityException) {
            // TODO handle
            Log.e(App.LOGCAT, "Security exception accessing Calendars list", e)
        } catch (e: Exception) {
            // TODO handle
            Log.e(App.LOGCAT, "Error getting Calendars", e)
        }
        return calendarList
    }

    fun getEvents(calendarsIds: List<Long>, daysAhead: Long): List<EventModel> {
        val eventModels: MutableList<EventModel> = ArrayList<EventModel>()
        val cr: ContentResolver = ctx.contentResolver
        Log.i(App.LOGCAT, "Getting Events for " + calendarsIds.size + " calendars")
        try {
            prepareEventsCursor(cr, calendarsIds, daysAhead)?.use { cur ->
                while (cur.moveToNext()) {
                    val event = EventModel(
// todo check for nulls
                        id = cur.getLong(cur.getColumnIndex(CalendarContract.Instances._ID)),
                        title = cur.getString(cur.getColumnIndex(CalendarContract.Instances.TITLE)),
                        dateStart = cur.getLong(cur.getColumnIndex(CalendarContract.Instances.BEGIN)).toLocalDateTime(),
                        dateEnd = cur.getLong(cur.getColumnIndex(CalendarContract.Instances.END)).toLocalDateTime(),
                        isAllDay = cur.getInt(cur.getColumnIndex(CalendarContract.Instances.ALL_DAY)) == BOOLEAN_TRUE,
                        eventColor = cur.getInt(cur.getColumnIndex(CalendarContract.Instances.DISPLAY_COLOR)),
                        calendarId = cur.getLong(cur.getColumnIndex(CalendarContract.Instances.CALENDAR_ID)),
                        eventId = cur.getLong(cur.getColumnIndex(CalendarContract.Instances.EVENT_ID)),
                    )

                    Log.d(App.LOGCAT, "Event read: $event")
                    eventModels.add(event)
                }
            }
        } catch (e: Exception) {
            // TODO handle
            Log.e(App.LOGCAT, "Error getting Events", e)
        }
        return eventModels
    }

    private fun prepareCalendarsCursor(cr: ContentResolver, uri: Uri): Cursor? {
        return cr.query(
            uri,
            arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.CALENDAR_COLOR,
                CalendarContract.Calendars.IS_PRIMARY
            ),
            null,
            null,
            CalendarContract.Calendars.ACCOUNT_NAME + ", " + CalendarContract.Calendars.IS_PRIMARY + " desc, " + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
        )
    }

    private fun prepareEventsCursor(cr: ContentResolver, calendarsIds: List<Long>, daysAhead: Long): Cursor? {
        val dateFrom = LocalDateTime.now()
        val dateTo = prepareDateTo(dateFrom, daysAhead)
        return cr.query(
            makeEventsUriForDates(dateFrom, dateTo),
            arrayOf(
                CalendarContract.Instances._ID,
                CalendarContract.Instances.EVENT_ID,
                CalendarContract.Instances.TITLE,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Instances.END,
                CalendarContract.Instances.ALL_DAY,
                CalendarContract.Instances.DISPLAY_COLOR,
                CalendarContract.Instances.CALENDAR_ID
            ),
            CalendarContract.Instances.CALENDAR_ID + " IN (" + prepareMultipleSubsPlaceholders(calendarsIds.size) + ")",
            calendarsIds.map { it.toString() }.toTypedArray(),
            CalendarContract.Instances.BEGIN + ", " + CalendarContract.Instances.END + ", " + CalendarContract.Instances.TITLE
        )
    }

    private fun prepareMultipleSubsPlaceholders(n: Int): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until n) {
            stringBuilder.append("?")
            if (i < n - 1) {
                stringBuilder.append(",")
            }
        }
        return stringBuilder.toString()
    }

    companion object {
        private const val BOOLEAN_TRUE = 1

        fun makeEventsObservationUri(): Uri {
            return makeEventsUriForDates(LocalDateTime.now(), null)
        }

        private fun makeEventsUriForDates(dateFrom: LocalDateTime, dateTo: LocalDateTime?): Uri =
            CalendarContract.Instances.CONTENT_URI.apply {
                buildUpon().appendPath(dateFrom.millis.toString()).apply {
                    dateTo?.let { appendPath(dateTo.millis.toString()) }
                }
            }


        private fun Long.toLocalDateTime() =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())


        private fun prepareDateTo(dateFrom: LocalDateTime, daysAhead: Long): LocalDateTime = dateFrom.toMidnightAtDay(daysAhead)
    }
}
