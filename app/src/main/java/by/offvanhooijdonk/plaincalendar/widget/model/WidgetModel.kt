package by.offvanhooijdonk.plaincalendar.widget.model

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color

//@Entity(tableName = "widgets")
data class WidgetModel(
    //@PrimaryKey
    val id: Long = 0,
    //@ColumnInfo(name = "days")
    val days: Int,
    //@ColumnInfo(name = "back_color")
    val backgroundColor: Long,
    //@ColumnInfo(name = "date_color")
    val textColor: Long,
    //@ColumnInfo(name = "opacity")
    val opacity: Float,
    //@ColumnInfo(name = "text_size_delta")
    val textSizeDelta: Int,
    //@ColumnInfo(name = "show_event_color")
    val showEventColor: Boolean,
    //@ColumnInfo(name = "show_today_date")
    val showTodayDate: Boolean,
    //@ColumnInfo(name = "show_today_day_of_week")
    val showTodayDayOfWeek: Boolean,
    //@ColumnInfo(name = "show_date_divide_line")
    val showDateDivider: Boolean,
    //@ColumnInfo(name = "show_end_date")
    val showEndDate: ShowEndDate,
    //@ColumnInfo(name = "show_today_leading_zero")
    val showTodayLeadingZero: Boolean,
    //@ColumnInfo(name = "show_date_text_label")
    val showDateTextLabel: Boolean,
    //@Ignore
    val calendars: List<CalendarModel> = emptyList(), // use to present on preview todo remove and use Calendars list on preview as an independent val
    val calendarIds: List<Long> = emptyList(), // use to store with widget info
) {
    enum class ShowEndDate(val code: Int) {
        NEVER(0), MORE_THAN_DAY(1), ALWAYS(2);

        companion object {
            fun fromCode(code: Int): ShowEndDate = values().getOrNull(code) ?: default

            val default = NEVER
        }
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
            showTodayDayOfWeek = true,
            showTodayLeadingZero = false,
        )
    }
}
