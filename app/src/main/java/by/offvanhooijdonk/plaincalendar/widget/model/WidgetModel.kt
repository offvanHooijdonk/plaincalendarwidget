package by.offvanhooijdonk.plaincalendar.widget.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "widgets")
data class WidgetModel(
    @PrimaryKey
    var id: Long = 0,
    @Ignore
    var calendars: List<CalendarModel> = emptyList(),
    @ColumnInfo(name = "days")
    var days: Int = 0,
    @ColumnInfo(name = "back_color")
    var backgroundColor: Int? = null,
    @ColumnInfo(name = "date_color")
    var textColor: Int? = null,
    @ColumnInfo(name = "opacity")
    var opacity: Float = 1.0f,
    @ColumnInfo(name = "text_size_delta")
    var textSizeDelta: Int? = null,
    @ColumnInfo(name = "show_event_color")
    var showEventColor: Boolean? = null,
    @ColumnInfo(name = "show_today_date")
    var showTodayDate: Boolean? = null,
    @ColumnInfo(name = "show_today_day_of_week")
    var showTodayDayOfWeek: Boolean? = null,
    @ColumnInfo(name = "show_date_divide_line")
    var showDateDivider: Boolean? = null,
    @ColumnInfo(name = "show_end_date")
    var showEndDate: ShowEndDate? = null,
    @ColumnInfo(name = "show_today_leading_zero")
    var showTodayLeadingZero: Boolean? = null,
    @ColumnInfo(name = "show_date_text_label")
    var showDateTextLabel: Boolean? = null,
) {
    enum class ShowEndDate(val code: Int) {
        NEVER(0), MORE_THAN_DAY(1), ALWAYS(2);

        companion object {
            fun fromCode(code: Int): ShowEndDate = values().getOrNull(code) ?: default

            val default = NEVER
        }
    }
}
