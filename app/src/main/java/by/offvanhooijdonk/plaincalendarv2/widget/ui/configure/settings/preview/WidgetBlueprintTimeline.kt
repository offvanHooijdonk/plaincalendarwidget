@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.WidgetItemShape
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.formatDateRangeLabel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getDateTextSize
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getTitleTextSize
import java.time.DayOfWeek
import java.time.LocalDateTime

@Composable
fun WidgetBlueprintTimeline(widget: WidgetModel) {
    WidgetEventWrapper(widget) {
        val events = previewEvents
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = D.listSpacingV)
        ) {
            itemsIndexed(events, key = { _, item -> item.id }) { index, item ->
                Column {
                    WidgetEventItem(item, widget)
                    if (widget.showEventDividers && (index < events.size - 1)) {
                        Divider(modifier = Modifier.padding(horizontal = D.eventItemPaddingH))
                    }
                }
            }
        }
    }
}

@Composable
private fun WidgetEventItem(event: EventModel, widget: WidgetModel) {
    Surface(
        onClick = {},
        shape = WidgetItemShape,
        color = Color.Transparent
    ) {
        val titleTextStyle = TextStyle(
            color = widget.textColor.toColor(),
            fontSize = getTitleTextSize(LocalContext.current, widget),
            fontWeight = if (widget.textStyleBold) FontWeight.Medium else FontWeight.Normal
        )

        val dateTextStyle = TextStyle(
            color = widget.textColor.toColor(),
            fontSize = getDateTextSize(LocalContext.current, widget),
            fontWeight = if (widget.textStyleBold) FontWeight.Medium else FontWeight.Normal
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = D.eventItemPaddingH, vertical = D.eventItemPaddingV)
        ) {
            Row {
                AnimatedContent(targetState = widget.showEventColor) { isShow ->
                    if (isShow) {
                        Spacer(modifier = Modifier.width(D.eventColorMarkSize + D.eventColorSpacing - D.spacingXS))
                    }
                }
                EventDateText(
                    dateStart = event.dateStart,
                    dateEnd = event.dateEnd,
                    isAllDayEvent = event.isAllDay,
                    showDayAsText = widget.showDateAsTextLabel,
                    showEndDate = widget.showEndDate,
                    textStyle = dateTextStyle,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EventColorMarkAnimated(
                    isShow = widget.showEventColor,
                    eventColor = widget.calendars.firstOrNull()?.color?.let { Color(it.toLong()) } ?: DefaultEventPreviewColor,
                    shape = widget.eventColorShape,
                )
                Text(text = event.title, style = titleTextStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
    textStyle: TextStyle
) {
    val dateText = formatDateRangeLabel(
        dateStart,
        dateEnd,
        isAllDayEvent,
        showDayAsText,
        showEndDate,
        ctx = LocalContext.current,
    )

    Text(
        text = dateText,
        style = textStyle,
    )
}

private val EventDateOne =
    LocalDateTime.now()
private val EventDateTwo =
    LocalDateTime.now().plusDays(1)
private val EventDateThree =
    LocalDateTime.now().plusDays(3)

private val previewEvents
    @Composable get() = listOf(
        EventModel(
            1,
            getEventTitle(EventDateOne.dayOfWeek),
            EventDateOne,
            EventDateOne.plusHours(2),
        ),
        EventModel(
            2,
            getEventTitle(EventDateTwo.dayOfWeek),
            EventDateTwo,
            EventDateTwo.plusDays(1).minusHours(1),
        ),
        EventModel(
            id = 3,
            title = getEventTitle(EventDateThree.dayOfWeek),
            isAllDay = true,
            dateStart = EventDateThree,
            dateEnd = EventDateThree.plusDays(1),
        ),
    )

@Composable
@ReadOnlyComposable
private fun getEventTitle(day: DayOfWeek): String = when (day) {
    DayOfWeek.MONDAY -> stringResource(R.string.sample_event_monday)
    DayOfWeek.TUESDAY -> stringResource(R.string.sample_event_tuesday)
    DayOfWeek.WEDNESDAY -> stringResource(R.string.sample_event_wednesday)
    DayOfWeek.THURSDAY -> stringResource(R.string.sample_event_thursday)
    DayOfWeek.FRIDAY -> stringResource(R.string.sample_event_friday)
    DayOfWeek.SATURDAY -> stringResource(R.string.sample_event_saturday)
    DayOfWeek.SUNDAY -> stringResource(R.string.sample_event_sunday)
}

@Preview
@Composable
private fun Preview_WidgetBlueprint() {
    MaterialTheme {
        WidgetBlueprintTimeline(DummyWidget.copy(opacity = 0.5f))
    }
}
