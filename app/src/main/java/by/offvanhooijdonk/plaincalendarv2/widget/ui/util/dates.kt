package by.offvanhooijdonk.plaincalendarv2.widget.ui.util

import android.content.Context
import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.isToday
import by.offvanhooijdonk.plaincalendarv2.widget.ext.isTomorrow
import by.offvanhooijdonk.plaincalendarv2.widget.ext.millis
import by.offvanhooijdonk.plaincalendarv2.widget.ext.timeTitle
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun createDateLabel(
    dateStart: LocalDateTime,
    dateEnd: LocalDateTime,
    isAllDayEvent: Boolean,
    showDayAsText: Boolean,
    showEndDate: WidgetModel.ShowEndDate,
    ctx: Context,
): String {
    val startDateText =
        createFullDateTimeText(date = dateStart, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText, ctx = ctx)
    return when (showEndDate) {
        WidgetModel.ShowEndDate.NEVER -> startDateText
        WidgetModel.ShowEndDate.MORE_THAN_DAY -> if (isOneDayEvent(dateStart, dateEnd)) {
            startDateText
        } else {
            val endDateText = createFullDateTimeText(dateEnd, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText, ctx = ctx)
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
                    createFullDateTimeText(dateEnd, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText, ctx = ctx)
                )
            }
        }
    }
}

@Composable
private fun createFullDateTimeText(
    date: LocalDateTime,
    isAllDayEvent: Boolean,
    showDayAsText: Boolean,
    ctx: Context
): String =
    when {
        showDayAsText && date.isToday -> ctx.getString(R.string.today)
        showDayAsText && date.isTomorrow -> ctx.getString(R.string.tomorrow)
        else -> DateUtils.formatDateTime(
            ctx,
            date.millis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_NO_YEAR
        )
    }.let { if (isAllDayEvent) it else ctx.getString(R.string.date_and_time_format, it, date.timeTitle) }

private fun isOneDayEvent(dateStart: LocalDateTime, dateEnd: LocalDateTime) =
    dateStart.truncatedTo(ChronoUnit.DAYS).isEqual(dateEnd.truncatedTo(ChronoUnit.DAYS))
