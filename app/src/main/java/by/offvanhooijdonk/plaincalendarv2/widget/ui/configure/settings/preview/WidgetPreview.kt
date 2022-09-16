@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutType
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.WidgetItemShape
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.createDateLabel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: WidgetModel) {
    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(targetState = widget.layoutType) { layout ->
            when (layout) {
                LayoutType.DEFAULT -> WidgetBlueprint(widget)
                LayoutType.EXTENDED -> Text(text = stringResource(R.string.widget_preview_missing))
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
        Column(
            modifier = Modifier.padding(
                horizontal = D.widgetPaddingH,
                vertical = D.widgetPaddingV
            )
        ) {
            val events = previewEvents
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = D.listSpacingV)
            ) {
                itemsIndexed(events, key = { _, item -> item.id }) { index, item ->
                    Column {
                        WidgetEventItem(item, widget)
                        if (widget.showEventDividers && (index < events.size - 1)) {
                            Divider()
                        }
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
        val textColor = widget.textColor.toColor()
        val textSizeDate =
            (LocalContext.current.resources.getInteger(R.integer.date_default_font_size_sp) + widget.textSizeDelta).sp
        val textSizeEvent =
            (LocalContext.current.resources.getInteger(R.integer.event_default_font_size_sp) + widget.textSizeDelta).sp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = D.eventItemPaddingH, vertical = D.eventItemPaddingV)
        ) {
            EventDateText(
                dateStart = event.dateStart,
                dateEnd = event.dateEnd,
                isAllDayEvent = event.isAllDay,
                showDayAsText = widget.showDateAsTextLabel,
                showEndDate = widget.showEndDate,
                textColor = textColor,
                textSize = textSizeDate
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedContent(targetState = widget.showEventColor) { showColor ->
                    when (showColor) {
                        true -> Row {
                            Icon(
                                modifier = Modifier.size(D.eventColorMarkSize),
                                painter = painterResource(R.drawable.ic_circle),
                                tint = widget.calendars.firstOrNull()?.color?.let { Color(it.toLong()) }
                                    ?: Color.Blue,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(D.eventColorSpacing))
                        }
                        false -> Unit
                    }
                }
                Text(text = event.title, color = textColor, fontSize = textSizeEvent, maxLines = 1)
            }
        }
    }
}

@Composable
private fun WidgetHeader(widget: WidgetModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val textColor = widget.textColor.toColor()
        val textSizeDate =
            (LocalContext.current.resources.getInteger(R.integer.date_default_font_size_sp) + widget.textSizeDelta).sp
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            color = textColor,
            fontSize = textSizeDate,
        )
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.ic_settings), tint = textColor, contentDescription = null
        )
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
    val dateText = createDateLabel(
        dateStart,
        dateEnd,
        isAllDayEvent,
        showDayAsText,
        showEndDate,
        ctx = LocalContext.current,
    )

    Text(
        text = dateText,
        color = textColor,
        fontSize = textSize,
    )
}

// todo move preview block somewhere

@Preview
@Composable
private fun Preview_WidgetBlueprint() {
    MaterialTheme {
        WidgetBlueprint(DummyWidget.copy(opacity = 0.5f))
    }
}

private val EventDateOne =
    LocalDateTime.now()
private val EventDateTwo =
    LocalDateTime.now().plusDays(1)
private val EventDateThree =
    LocalDateTime.now().plusDays(3)

val previewEvents
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
