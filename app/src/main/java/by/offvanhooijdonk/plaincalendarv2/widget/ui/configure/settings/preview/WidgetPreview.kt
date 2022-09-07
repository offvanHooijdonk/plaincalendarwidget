@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import android.text.format.DateUtils
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.isToday
import by.offvanhooijdonk.plaincalendarv2.widget.ext.isTomorrow
import by.offvanhooijdonk.plaincalendarv2.widget.ext.millis
import by.offvanhooijdonk.plaincalendarv2.widget.ext.timeTitle
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutType
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.WidgetItemShape
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: WidgetModel) {
    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(targetState = widget.layoutType) { layout ->
            when (layout) {
                LayoutType.DEFAULT -> WidgetBlueprint(widget)
                LayoutType.EXTENDED -> Text(text = "Nothing to show yet")
            }
        }
    }
}

@Composable
private fun WidgetBlueprint(widget: WidgetModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(widget.backgroundColor.toULong()).copy(alpha = widget.opacity),
        shape = RoundedCornerShape(D.widgetCornerRadius),
    ) {
        Box(modifier = Modifier.padding(horizontal = D.widgetPaddingH, vertical = D.widgetPaddingV)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = D.listSpacingV)
            ) {
                itemsIndexed(previewEvents, key = { _, item -> item.id }) { index, item ->
                    Column {
                        WidgetEventItem(item, widget)
                        if (widget.showEventDividers && (index < previewEvents.size - 1)) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WidgetEventItem(event: EventModel, widgetModel: WidgetModel) {
    Surface(
        onClick = {},
        shape = WidgetItemShape,
        color = Color.Transparent
    ) {
        val textColor = widgetModel.textColor.toColor()
        val textSize = (LocalContext.current.resources.getInteger(R.integer.default_font_size_sp) + widgetModel.textSizeDelta).sp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = D.eventItemPaddingH, vertical = D.eventItemPaddingV)
        ) {
            EventDateText(
                dateStart = event.dateStart,
                dateEnd = event.dateEnd,
                isAllDayEvent = event.isAllDay,
                showDayAsText = widgetModel.showDateAsTextLabel,
                showEndDate = widgetModel.showEndDate,
                textColor = textColor,
                textSize = textSize
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(targetState = widgetModel.showEventColor) { showColor ->
                    when (showColor) {
                        true -> Row {
                            Icon(
                                modifier = Modifier.size(D.eventColorMarkSize),
                                painter = painterResource(R.drawable.ic_circle),
                                tint = widgetModel.calendars.firstOrNull()?.color?.let { Color(it.toLong()) } ?: Color.Blue,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(D.eventColorSpacing))
                        }
                        false -> Unit
                    }
                }
                Text(text = event.title, color = textColor, fontSize = textSize, maxLines = 1)
            }
        }
    }
}

@Composable
private fun EventDateText(
    dateStart: LocalDateTime,
    dateEnd: LocalDateTime,
    isAllDayEvent: Boolean,
    showDayAsText: Boolean,
    showEndDate: WidgetModel.ShowEndDate,
    textColor: Color,
    textSize: TextUnit
) {
    val dateText = createDateLabel(dateStart, dateEnd, isAllDayEvent, showDayAsText, showEndDate)

    Text(
        text = dateText,
        color = textColor,
        fontSize = textSize,
    )
}

@Composable
private fun createDateLabel(
    dateStart: LocalDateTime,
    dateEnd: LocalDateTime,
    isAllDayEvent: Boolean,
    showDayAsText: Boolean,
    showEndDate: WidgetModel.ShowEndDate,
): String {
    val startDateText = createFullDateTimeText(date = dateStart, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText)
    return when (showEndDate) {
        WidgetModel.ShowEndDate.NEVER -> startDateText
        WidgetModel.ShowEndDate.MORE_THAN_DAY -> if (isOneDayEvent(dateStart, dateEnd)) {
            startDateText
        } else {
            val endDateText = createFullDateTimeText(dateEnd, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText)
            stringResource(R.string.date_range_format, startDateText, endDateText)
        }
        WidgetModel.ShowEndDate.ALWAYS -> {
            val isOneDayEvent = isOneDayEvent(dateStart, dateEnd)
            when {
                isOneDayEvent && !isAllDayEvent -> stringResource(R.string.date_range_format, startDateText, dateEnd.timeTitle)
                isOneDayEvent && isAllDayEvent -> startDateText
                else -> stringResource(
                    R.string.date_range_format,
                    startDateText,
                    createFullDateTimeText(dateEnd, isAllDayEvent = isAllDayEvent, showDayAsText = showDayAsText)
                )
            }
        }
    }
}

@Composable
private fun createFullDateTimeText(date: LocalDateTime, isAllDayEvent: Boolean, showDayAsText: Boolean): String =
    when {
        showDayAsText && date.isToday -> stringResource(R.string.today)
        showDayAsText && date.isTomorrow -> stringResource(R.string.tomorrow)
        else -> DateUtils.formatDateTime(
            LocalContext.current,
            date.millis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_NO_YEAR
        )
    }.let { if (isAllDayEvent) it else stringResource(R.string.date_and_time_format, it, date.timeTitle) }

private fun isOneDayEvent(dateStart: LocalDateTime, dateEnd: LocalDateTime) =
    dateStart.truncatedTo(ChronoUnit.DAYS).isEqual(dateEnd.truncatedTo(ChronoUnit.DAYS))

@Preview
@Composable
private fun Preview_WidgetBlueprint() {
    MaterialTheme {
        WidgetBlueprint(DummyWidget.copy(opacity = 0.5f))
    }
}

private val Wednesday = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.WEDNESDAY.value.toLong())
private val Thursday = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.THURSDAY.value.toLong())
private val Sunday = LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.value.toLong())

val previewEvents = listOf(
    EventModel(
        1,
        "Bicycling on every Wednesday evening",
        Wednesday,
        Wednesday.plusHours(2),
    ),
    EventModel(
        2,
        "Go waltzing to the zoo",
        Thursday,
        Thursday.plusDays(1).minusHours(1),
    ),
    EventModel(
        id = 3,
        title = "Lazing on a Sunday afternoon",
        isAllDay = true,
        dateStart = Sunday,
        dateEnd = Sunday.plusDays(1),
    ),
)
