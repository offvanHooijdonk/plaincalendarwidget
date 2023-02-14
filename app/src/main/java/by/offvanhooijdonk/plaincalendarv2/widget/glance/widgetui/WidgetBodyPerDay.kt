package by.offvanhooijdonk.plaincalendarv2.widget.glance.widgetui

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.glanceDimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.formatDateLabel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.formatTimeLabel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getDateTextSize
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getTitleTextSize

@Composable
fun WidgetBodyPerDay(events: List<EventModel>, widget: WidgetModel) {
    val backColor = widget.backgroundColor.toColor()
    val opacity = widget.opacity
    val textStyleDate = TextStyle(
        fontSize = getDateTextSize(LocalContext.current, widget),
        color = ColorProvider(widget.textColor.toColor()),
        fontWeight = if (widget.textStyleBold) FontWeight.Medium else FontWeight.Normal
    )
    val textStyleTitle = TextStyle(
        fontSize = getTitleTextSize(LocalContext.current, widget),
        color = ColorProvider(widget.textColor.toColor()),
        fontWeight = if (widget.textStyleBold) FontWeight.Medium else FontWeight.Normal
    )

    Box(
        modifier = GlanceModifier
            .padding(horizontal = glanceDimens().widgetPaddingH, vertical = glanceDimens().widgetPaddingV)
            .background(backColor.copy(alpha = opacity))
            .appWidgetBackground()
            .fillMaxSize()
    ) {
        LazyColumn(modifier = GlanceModifier.padding(horizontal = glanceDimens().widgetPaddingH)) {
            if (events.isEmpty()) {
                item {
                    EmptyEventsMessage(textStyle = textStyleTitle)
                }
            }
            events.groupBy { it.dateStart.toLocalDate() }.forEach { (dayDate, events) ->
                item(itemId = dayDate.toEpochDay()) {
                    Box(modifier = GlanceModifier.padding(top = glanceDimens().spacingS, bottom = glanceDimens().spacingXS)) {
                        Text(
                            text = formatDateLabel(LocalContext.current, dayDate.atStartOfDay(), widget.showDateAsTextLabel),
                            style = textStyleDate,
                        )
                    }
                }

                itemsIndexed(events, itemId = { _, item -> item.id }) { index, event ->
                    Column(modifier = GlanceModifier.clickable(actionStartActivity(createOpenEventIntent(event.eventId)))) {
                        EventItem(event, widget, textStyleTitle, textStyleDate)

                        if (widget.showEventDividers && (index < events.size - 1)) {
                            EventsDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventItem(event: EventModel, widget: WidgetModel, textStyleTitle: TextStyle, textStyleDate: TextStyle) {
    Row(
        modifier = GlanceModifier.padding(horizontal = glanceDimens().eventItemPaddingH, vertical = glanceDimens().eventItemPaddingV),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (widget.showEventColor) {
            EventColorMark(event.colorValue, widget.eventColorShape)
        }
        Text(
            text = formatTimeLabel(
                ctx = LocalContext.current,
                dateStart = event.dateStart,
                dateEnd = event.dateEnd,
                isAllDayEvent = event.isAllDay,
                isShowEndDate = widget.showEndDate == WidgetModel.ShowEndDate.ALWAYS,
            ), style = textStyleDate
        )
        Spacer(modifier = GlanceModifier.width(glanceDimens().spacingS))
        Text(text = event.title, style = textStyleTitle, maxLines = 1)
    }
}
