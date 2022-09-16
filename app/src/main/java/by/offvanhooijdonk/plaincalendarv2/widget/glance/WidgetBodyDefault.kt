package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.GD
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.createDateLabel

@Composable
fun WidgetBodyDefault(events: List<EventModel>, model: WidgetModel) {
    val dividerColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
    val backColor = model.backgroundColor.toColor()
    val opacity = model.opacity
    val textStyleDate = TextStyle(
        fontSize = (LocalContext.current.resources.getInteger(R.integer.date_default_font_size_sp) + model.textSizeDelta).sp,
        color = ColorProvider(model.textColor.toColor())
    )
    val textStyleEvent = TextStyle(
        fontSize = (LocalContext.current.resources.getInteger(R.integer.event_default_font_size_sp) + model.textSizeDelta).sp,
        color = ColorProvider(model.textColor.toColor())
    )

    Box(
        modifier = GlanceModifier
            .padding(horizontal = GD.widgetPaddingH, vertical = GD.widgetPaddingV)
            .background(backColor.copy(alpha = opacity))
            .appWidgetBackground()
            .fillMaxSize()
    ) {
        LazyColumn {
            itemsIndexed(events, itemId = { _, item -> item.eventId }) { index, event ->
                Column {
                    Column(
                        modifier = GlanceModifier
                            .clickable(actionStartActivity(createOpenEventIntent(event.eventId)))
                            .padding(horizontal = GD.eventItemPaddingH, vertical = GD.eventItemPaddingV),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = createDateLabel(
                                event.dateStart,
                                event.dateEnd,
                                event.isAllDay,
                                model.showDateAsTextLabel,
                                model.showEndDate,
                                ctx = LocalContext.current,
                            ),
                            style = textStyleDate
                        )
                        Row(modifier = GlanceModifier.padding(start = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (model.showEventColor) {
                                Box(
                                    modifier = GlanceModifier
                                        .size(GD.eventColorMarkSize)
                                        .background(event.eventColor?.let { Color(it.toLong()) } ?: Color.White)
                                        .cornerRadius(4.dp) // todo use dimens or image
                                ) {}
                                Spacer(modifier = GlanceModifier.width(GD.eventColorSpacing))
                            }
                            Text(text = event.title, style = textStyleEvent, maxLines = 1)
                        }
                    }
                    if (model.showEventDividers && (index < events.size - 1)) {
                        Box(
                            modifier = GlanceModifier.height(1.dp).fillMaxWidth()
                                .background(dividerColor)
                        ) {}
                    }
                }
            }
        }
    }
}

private fun createOpenEventIntent(eventId: Long) =
    Intent(Intent.ACTION_VIEW).apply { data = CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(eventId.toString()).build() }
