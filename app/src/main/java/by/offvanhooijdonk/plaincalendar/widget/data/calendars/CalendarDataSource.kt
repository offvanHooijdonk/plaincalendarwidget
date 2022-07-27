package by.offvanhooijdonk.plaincalendar.widget.data.calendars

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import by.offvanhooijdonk.plaincalendarv2.widget.app.App
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.EventModel
import java.lang.Exception
import java.lang.StringBuilder
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class CalendarDataSource(private val ctx: Context) {
    fun requestCalendarList(): List<CalendarModel> {
        return loadCalendars()
    }

    private fun loadCalendars(): List<CalendarModel> {
        val calendarList: MutableList<CalendarModel> = ArrayList<CalendarModel>()
        val cr: ContentResolver = ctx.contentResolver
        val uri: Uri = CalendarContract.Calendars.CONTENT_URI
        try {
            prepareCalendarsCursor(cr, uri)?.use { cur ->
                while (cur.moveToNext()) {
                    val calendar = CalendarModel( // todo check for nulls
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

    fun getEvents(calendars: List<CalendarModel>, daysAhead: Int): List<EventModel> {
        val eventModels: MutableList<EventModel> = ArrayList<EventModel>()
        val cr: ContentResolver = ctx.contentResolver
        Log.i(App.LOGCAT, "Getting Events for " + calendars.size + " calendars")
        try {
            prepareEventsCursor(cr, calendars, daysAhead)?.use { cur ->
                while (cur.moveToNext()) {
                    val event = EventModel(// todo check for nulls
                        id = cur.getLong(cur.getColumnIndex(CalendarContract.Instances._ID)),
                        title = cur.getString(cur.getColumnIndex(CalendarContract.Instances.TITLE)),
                        dateStart = Date(cur.getLong(cur.getColumnIndex(CalendarContract.Instances.BEGIN))),
                        dateEnd = Date(cur.getLong(cur.getColumnIndex(CalendarContract.Instances.END))),
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
            CalendarContract.Calendars.ACCOUNT_NAME + ", " + CalendarContract.Calendars.IS_PRIMARY + " desc, " + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
    }

    private fun prepareEventsCursor(cr: ContentResolver, calendars: List<CalendarModel>, daysAhead: Int): Cursor? {
        val dateFrom = Date()
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
            CalendarContract.Instances.CALENDAR_ID + " IN (" + prepareMultipleSubsPlaceholders(calendars.size) + ")",
            prepareEventsArgs(calendars),
            CalendarContract.Instances.BEGIN + ", " + CalendarContract.Instances.END + ", " + CalendarContract.Instances.TITLE)
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

    private fun prepareEventsArgs(calendars: List<CalendarModel>): Array<String?> {
        val args = arrayOfNulls<String>(calendars.size)
        var i = 0
        for (bean in calendars) {
            args[i] = bean.id.toString()
            i++
        }
        return args
    }

    companion object {
        private const val BOOLEAN_TRUE = 1
        fun makeEventsObservationUri(): Uri {
            return makeEventsUriForDates(Date(), null)
        }

        private fun makeEventsUriForDates(dateFrom: Date?, dateTo: Date?): Uri {
            var uri: Uri = CalendarContract.Instances.CONTENT_URI
            if (dateFrom != null) {
                val uriBuilder = uri.buildUpon().appendPath(dateFrom.time.toString())
                if (dateTo != null) {
                    uriBuilder.appendPath(dateTo.time.toString())
                }
                uri = uriBuilder.build()
            }
            return uri
        }

        private fun prepareDateTo(dateFrom: Date, daysAhead: Int): Date {
            val calendarTo = Calendar.getInstance()
            calendarTo.time = dateFrom
            calendarTo.add(Calendar.DAY_OF_MONTH, daysAhead)
            calendarTo[Calendar.HOUR_OF_DAY] = 0
            calendarTo[Calendar.MINUTE] = 0
            calendarTo[Calendar.SECOND] = 0
            calendarTo.add(Calendar.SECOND, -1)
            return calendarTo.time
        }
    }
}
