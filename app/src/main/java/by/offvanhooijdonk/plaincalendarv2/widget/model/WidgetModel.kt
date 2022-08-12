package by.offvanhooijdonk.plaincalendarv2.widget.model

import androidx.compose.ui.graphics.Color
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutType

data class WidgetModel(
    val id: Long = 0,
    val days: Int,
    val backgroundColor: Long,
    val textColor: Long,
    val opacity: Float,
    val textSizeDelta: Int,
    val showEventColor: Boolean,
    val showTodayDate: Boolean,
    val showTodayDayOfWeek: Boolean,
    val showDateDivider: Boolean,
    val showEndDate: ShowEndDate,
    val showTodayLeadingZero: Boolean,
    val showDateTextLabel: Boolean,
    val layoutType: LayoutType,
    val calendars: List<CalendarModel> = emptyList(), // use to present on preview todo remove and use Calendars list on preview as an independent val
    val calendarIds: List<Long> = emptyList(), // use to store with widget info
) {
    enum class ShowEndDate(val code: Int) {
        NEVER(0), MORE_THAN_DAY(1), ALWAYS(2);
    }

    companion object {
        fun createDefault() = WidgetModel(
            days = 7,
            backgroundColor = Color.White.value.toLong(),
            opacity = 1.0f,
            textColor = Color.Black.value.toLong(),
            textSizeDelta = 0,
            showEventColor = true,
            showDateTextLabel = true,
            showDateDivider = true,
            showEndDate = ShowEndDate.MORE_THAN_DAY,
            showTodayDate = true,
            layoutType = LayoutType.default,
            showTodayDayOfWeek = true,
            showTodayLeadingZero = false,
        )
    }
}
