package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.*
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun WidgetBlueprintPerDay(widget: WidgetModel) {
    WidgetEventWrapper(widget) {
        val events = previewEvents

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

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = D.listSpacingV, horizontal = D.eventItemPaddingH)
        ) {
            events.groupBy { it.dateStart.toLocalDate() }.forEach { (dayDate, dayEvents) ->
                item(key = dayDate) {
                    EventDayLabelItem(dayDate, dateTextStyle, widget.showDateAsTextLabel)
                }
                itemsIndexed(dayEvents, key = { _, item -> item.id }) { index, event ->
                    Column {
                        EventItem(
                            eventModel = event,
                            titleTextStyle = titleTextStyle,
                            dateTextStyle = dateTextStyle,
                            eventColor = widget.calendars.firstOrNull()?.color?.let { Color(it.toLong()) } ?: DefaultEventPreviewColor,
                            isShowEventColor = widget.showEventColor,
                            isShowEndDate = widget.showEndDate == WidgetModel.ShowEndDate.ALWAYS
                        )

                        if (widget.showEventDividers && index < dayEvents.size - 1) {
                            Spacer(modifier = Modifier.height(D.spacingXS))
                            Divider(Modifier.padding(start = D.spacingM))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventDayLabelItem(
    day: LocalDate,
    dateTextStyle: TextStyle,
    isShowDateAsText: Boolean
) {
    Box(modifier = Modifier.padding(top = D.spacingS, bottom = D.spacingXS)) {
        Text(
            text = formatDateLabel(LocalContext.current, day.atStartOfDay(), isShowDateAsText),
            style = dateTextStyle
        )
    }
}

@Composable
private fun EventItem(
    eventModel: EventModel,
    titleTextStyle: TextStyle,
    dateTextStyle: TextStyle,
    eventColor: Color,
    isShowEventColor: Boolean,
    isShowEndDate: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = D.spacingM, vertical = D.spacingXS),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EventColorMarkAnimated(
            isShow = isShowEventColor,
            eventColor = eventColor
        )
        Text(
            modifier = Modifier.alignByBaseline(),
            text = formatTimeLabel(
                ctx = LocalContext.current,
                dateStart = eventModel.dateStart,
                dateEnd = eventModel.dateEnd,
                isAllDayEvent = eventModel.isAllDay,
                isShowEndDate = isShowEndDate,
            ),
            style = dateTextStyle
        ) // todo show end/'all day' date if configured
        Spacer(Modifier.width(D.spacingS))
        Text(
            modifier = Modifier.alignByBaseline(),
            text = eventModel.title,
            style = titleTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun Preview_WidgetBlueprintDaily() {
    MaterialTheme {
        WidgetBlueprintPerDay(DummyWidget.copy(opacity = 0.5f))
    }
}

private val Today = LocalDateTime.now()

private val previewEvents: List<EventModel>
    @Composable
    get() = listOf(
        EventModel(
            id = 1,
            title = stringResource(R.string.sample_event_sunday),
            dateStart = Today,
            dateEnd = Today.plusHours(1),
        ),
        EventModel(
            id = 3,
            title = stringResource(R.string.sample_event_monday),
            dateStart = Today.plusHours(3).plusMinutes(30),
            dateEnd = Today.plusHours(4),
        ),
        EventModel(
            id = 15,
            title = stringResource(R.string.sample_event_tuesday),
            dateStart = Today.plusDays(2),
            dateEnd = Today.plusDays(2).plusHours(1),
        ),
        EventModel(
            id = 20,
            title = stringResource(R.string.sample_event_wednesday),
            dateStart = Today.plusDays(3),
            dateEnd = Today.plusDays(4),
            isAllDay = true,
        ),
    )
