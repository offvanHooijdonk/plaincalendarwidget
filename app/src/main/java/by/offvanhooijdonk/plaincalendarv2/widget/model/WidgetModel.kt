package by.offvanhooijdonk.plaincalendarv2.widget.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutType

data class WidgetModel(
    val id: Long = 0,
    val days: Int,
    val backgroundColor: Long,
    val textColor: Long,
    val opacity: Float,
    val textSizeDelta: Int,
    /** Show marks (circles) with event color */
    val showEventColor: Boolean,
    /** Show date (at the top of widget?) */
    val showTodayDate: Boolean,
    val showEventDividers: Boolean,
    val showEndDate: ShowEndDate,
    /** Show 'today'/'tomorrow' dates as corresponding text */
    val showDateAsTextLabel: Boolean,
    val layoutType: LayoutType,
    val calendars: List<CalendarModel> = emptyList(), // use to present on preview todo remove and use Calendars list on preview as an independent val
    val calendarIds: List<Long> = emptyList(), // use to store with widget info
) {
    enum class ShowEndDate {
        NEVER, MORE_THAN_DAY, ALWAYS;

        val title: String
            @Composable get() = when (this) {
                NEVER -> R.string.event_end_option_never
                MORE_THAN_DAY -> R.string.event_end_option_if_next_day
                ALWAYS -> R.string.event_end_option_always
            }.let { stringResource(it) }

        companion object {
            val default = ALWAYS

            fun valueOfOrDefault(value: String) = try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }

    companion object {
        fun createDefault(id: Long? = null) = WidgetModel(
            id = id ?: 0,
            days = 7,
            backgroundColor = Color.White.value.toLong(),
            opacity = 1.0f,
            textColor = Color.Black.value.toLong(),
            textSizeDelta = 0,
            showEventColor = true,
            showDateAsTextLabel = true,
            showEventDividers = true,
            showEndDate = ShowEndDate.default,
            showTodayDate = true,
            layoutType = LayoutType.default,
        )
    }
}

val DummyWidget = WidgetModel.createDefault()
