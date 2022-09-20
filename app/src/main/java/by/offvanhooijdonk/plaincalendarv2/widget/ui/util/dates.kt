package by.offvanhooijdonk.plaincalendarv2.widget.ui.util

import android.content.Context
import android.text.format.DateUtils
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.isToday
import by.offvanhooijdonk.plaincalendarv2.widget.ext.isTomorrow
import by.offvanhooijdonk.plaincalendarv2.widget.ext.millis
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

fun formatDateRangeLabel(
    dateStart: LocalDateTime,
    dateEnd: LocalDateTime,
    isAllDayEvent: Boolean,
    showDayAsText: Boolean,
    showEndDate: WidgetModel.ShowEndDate,
    ctx: Context,
): String {
    val startDateText =
        formatFullDateTimeText(date = dateStart, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText, ctx = ctx)
    return when (showEndDate) {
        WidgetModel.ShowEndDate.NEVER -> startDateText
        WidgetModel.ShowEndDate.MORE_THAN_DAY -> if (isOneDayEvent(dateStart, dateEnd)) {
            startDateText
        } else {
            val endDateText = formatFullDateTimeText(dateEnd, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText, ctx = ctx)
            ctx.getString(R.string.date_range_format, startDateText, endDateText)
        }
        WidgetModel.ShowEndDate.ALWAYS -> {
            val isOneDayEvent = isOneDayEvent(dateStart, dateEnd)
            when {
                isOneDayEvent && !isAllDayEvent -> ctx.getString(R.string.date_range_format, startDateText, dateEnd.timeTitle)
                isOneDayEvent && isAllDayEvent -> startDateText
                else -> ctx.getString(
                    R.string.date_range_format,
                    startDateText,
                    formatFullDateTimeText(dateEnd, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText, ctx = ctx)
                )
            }
        }
    }
}

fun formatTimeLabel(
    ctx: Context,
    dateStart: LocalDateTime,
    dateEnd: LocalDateTime,
    isAllDayEvent: Boolean,
    isShowEndDate: Boolean
): String =
    when {
        isAllDayEvent -> ctx.getString(R.string.date_all_day)
        isShowEndDate -> ctx.getString(R.string.date_range_format, dateStart.timeTitle, dateEnd.timeTitle)
        else -> dateStart.timeTitle
    }

fun formatDateLabel(ctx: Context, date: LocalDateTime, showDayAsText: Boolean): String =
    when {
        showDayAsText && date.isToday -> ctx.getString(R.string.today)
        showDayAsText && date.isTomorrow -> ctx.getString(R.string.tomorrow)
        else -> formatDate(ctx, date)
    }

private fun formatDate(context: Context, date: LocalDateTime): String = DateUtils.formatDateTime(
    context,
    date.millis,
    DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_NO_YEAR
)

private fun formatFullDateTimeText(
    date: LocalDateTime,
    isAllDayEvent: Boolean,
    showDayAsText: Boolean,
    ctx: Context
): String =
    formatDateLabel(ctx, date, showDayAsText).let {
        if (isAllDayEvent) it else ctx.getString(
            R.string.date_and_time_format,
            it,
            date.timeTitle
        )
    }

private val timeFormat = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

val LocalDateTime.timeTitle: String
    get() = this.format(timeFormat)

private fun isOneDayEvent(dateStart: LocalDateTime, dateEnd: LocalDateTime) =
    dateStart.truncatedTo(ChronoUnit.DAYS).isEqual(dateEnd.truncatedTo(ChronoUnit.DAYS))
