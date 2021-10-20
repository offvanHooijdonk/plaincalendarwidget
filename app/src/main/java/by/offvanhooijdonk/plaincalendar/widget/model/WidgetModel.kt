package by.offvanhooijdonk.plaincalendar.widget.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "widgets")
data class WidgetModel(
    @PrimaryKey
    val id: Long = 0,
    @Ignore
    val calendars: List<CalendarModel> = emptyList(),
    @ColumnInfo(name = "days")
    val days: Int = 0,
    @ColumnInfo(name = "back_color")
    val backgroundColor: Int? = null,
    @ColumnInfo(name = "date_color")
    val textColor: Int? = null,
    @ColumnInfo(name = "opacity")
    val opacity: Int? = null,
    @ColumnInfo(name = "text_size_delta")
    val textSizeDelta: Int? = null,
    @ColumnInfo(name = "show_event_color")
    val showEventColor: Boolean? = null,
    @ColumnInfo(name = "show_today_date")
    val showTodayDate: Boolean? = null,
    @ColumnInfo(name = "show_today_day_of_week")
    val showTodayDayOfWeek: Boolean? = null,
    @ColumnInfo(name = "show_date_divide_line")
    val showDateDivider: Boolean? = null,
    @ColumnInfo(name = "show_end_date")
    val showEndDate: ShowEndDate? = null,
    @ColumnInfo(name = "show_today_leading_zero")
    val showTodayLeadingZero: Boolean? = null,
    @ColumnInfo(name = "show_date_text_label")
    val showDateTextLabel: Boolean? = null,
) {
    override fun equals(other: Any?): Boolean =
        (other as? WidgetModel)?.id == id

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return """
            {id=$id
            ,backgroundColor=$backgroundColor
            ,dateColor=$textColor
            }
            """.trimIndent()
    }

    enum class Corners(val code: Int) {
        NO_CORNER(0), SMALL(1), MEDIUM(2), LARGE(3), XLARGE(4);

        companion object {
            fun fromCode(code: Int): Corners = values().getOrNull(code) ?: default

            val default = SMALL
        }
    }

    enum class ShowEndDate(val code: Int) {
        NEVER(0), MORE_THAN_DAY(1), ALWAYS(2);

        companion object {
            fun fromCode(code: Int): ShowEndDate = values().getOrNull(code) ?: default

            val default = NEVER
        }
    }
}
