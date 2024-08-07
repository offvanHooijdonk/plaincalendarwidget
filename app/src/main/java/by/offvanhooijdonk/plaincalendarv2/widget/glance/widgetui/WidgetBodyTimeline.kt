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
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.formatDateRangeLabel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getDateTextSize
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.getTitleTextSize

@Composable
fun WidgetBodyTimeline(events: List<EventModel>, model: WidgetModel) {
    // todo create class for ui settings
    val backColor = model.backgroundColor.toColor()
    val opacity = model.opacity
    val textStyleDate = TextStyle(
        fontSize = getDateTextSize(LocalContext.current, model),
        color = ColorProvider(model.textColor.toColor()),
        fontWeight = if (model.textStyleBold) FontWeight.Medium else FontWeight.Normal
    )
    val textStyleEvent = TextStyle(
        fontSize = getTitleTextSize(LocalContext.current, model),
        color = ColorProvider(model.textColor.toColor()),
        fontWeight = if (model.textStyleBold) FontWeight.Medium else FontWeight.Normal
    )
    // todo ----

    Box(
        modifier = GlanceModifier
            .padding(horizontal = glanceDimens().widgetPaddingH, vertical = glanceDimens().widgetPaddingV)
            .background(backColor.copy(alpha = opacity))
            .appWidgetBackground()
            .fillMaxSize()
    ) {
        LazyColumn {
            if (events.isEmpty()) {
                item {
                    EmptyEventsMessage(textStyle = textStyleEvent)
                }
            }
            itemsIndexed(events, itemId = { _, item -> item.eventId }) { index, event ->
                Column {
                    Column(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .padding(horizontal = glanceDimens().eventItemPaddingH, vertical = glanceDimens().eventItemPaddingV)
                            .clickable(actionStartActivity(createOpenEventIntent(event.eventId))),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row {
                            Spacer(
                                modifier = GlanceModifier.width(
                                    if (model.showEventColor) glanceDimens().eventColorMarkSize + glanceDimens().eventColorSpacing + glanceDimens().spacingXXS else glanceDimens().spacingXXS
                                )
                            )

                            Text(
                                text = formatDateRangeLabel(
                                    event.dateStart,
                                    event.dateEnd,
                                    event.isAllDay,
                                    model.showDateAsTextLabel,
                                    model.showEndDate,
                                    ctx = LocalContext.current,
                                ),
                                style = textStyleDate
                            )
                        }
                        Row(
                            modifier = GlanceModifier.padding(start = glanceDimens().spacingXS),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.Start,
                        ) {
                            if (model.showEventColor) {
                                EventColorMark(event.colorValue, model.eventColorShape)
                            }
                            Text(text = event.title, style = textStyleEvent, maxLines = 1)
                        }
                    }
                    if (model.showEventDividers && (index < events.size - 1)) {
                        Spacer(modifier = GlanceModifier.height(glanceDimens().spacingXXS))
                        EventsDivider()
                        Spacer(modifier = GlanceModifier.height(glanceDimens().spacingXXS))
                    }
                }
            }
        }
    }
}
