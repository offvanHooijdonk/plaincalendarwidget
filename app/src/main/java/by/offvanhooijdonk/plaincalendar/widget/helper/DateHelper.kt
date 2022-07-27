package by.offvanhooijdonk.plaincalendar.widget.helper

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel.ShowEndDate
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

var MILLIS_IN_DAY = (24 * 60 * 60 * 1000).toLong()
private val EVENT_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM)
private val EVENT_TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT)

@SuppressLint("SimpleDateFormat")
private val SDF_DATE_ONLY = SimpleDateFormat("d") // todo improve code

@SuppressLint("SimpleDateFormat")
private val SDF_DATE_ONLY_LEAD_ZERO = SimpleDateFormat("dd") // todo improve code

@SuppressLint("SimpleDateFormat")
private val SDF_DAY = SimpleDateFormat("EE") // todo improve code

fun formatDateOnly(date: Date, leadingZero: Boolean): String {
    return if (leadingZero) {
        SDF_DATE_ONLY_LEAD_ZERO.format(date)
    } else {
        SDF_DATE_ONLY.format(date)
    }
}

fun formatDay(date: Date): String {
    return SDF_DAY.format(date)
}

val closestMidnightMillis: Long // todo extension to Calendar instance
    get() {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return calendar.timeInMillis
    }

// todo improve code readability
fun formatEventDateRange(ctx: Context, dateStart: Date, dateEnd: Date, isAllDay: Boolean, useLabels: Boolean, endDateRule: ShowEndDate): String {
    val textDateStart = if (isAllDay) formatEventDate(ctx, dateStart, useLabels) else ctx.getString(R.string.format_date_plus_time,
        formatEventDate(ctx, dateStart, useLabels),
        formatEventTime(dateStart))
    val isShowEndDate = endDateRule == ShowEndDate.ALWAYS || endDateRule == ShowEndDate.MORE_THAN_DAY && !isSameDay(dateStart, dateEnd)
    val textDateEnd = if (isShowEndDate) {
        ctx.getString(R.string.date_range_separator) +
        if (isSameDay(dateStart, dateEnd))
            formatEventTime(dateEnd)
        else
            ctx.getString(R.string.format_date_plus_time,
                formatEventDate(ctx, dateEnd, useLabels),
                if (isAllDay) "" else formatEventTime(dateEnd))
    } else ""
    return "$textDateStart$textDateEnd"
}

private fun formatEventDate(ctx: Context, date: Date, useLabels: Boolean): String {
    val calToday = Calendar.getInstance()
    val calNextDay = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 1) }
    val calEvent = Calendar.getInstance().apply { time = date }
    return when {
        useLabels && isSameDay(calToday, calEvent) -> ctx.getString(R.string.today)
        useLabels && isSameDay(calNextDay, calEvent) -> ctx.getString(R.string.tomorrow)
        else -> {
            val flags = DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or
                if (calEvent[Calendar.YEAR] != calToday[Calendar.YEAR]) DateUtils.FORMAT_NO_YEAR else 0
            DateUtils.formatDateTime(ctx, date.time, flags)
        }
    }
}

private fun formatEventTime(date: Date): String {
    return EVENT_TIME_FORMAT.format(date)
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance()
    cal1.time = date1
    val cal2 = Calendar.getInstance()
    cal2.time = date2
    return isSameDay(cal1, cal2)
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1[Calendar.DAY_OF_MONTH] == cal2[Calendar.DAY_OF_MONTH] && cal1[Calendar.MONTH] == cal2[Calendar.MONTH] && cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
}
