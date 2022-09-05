package by.offvanhooijdonk.plaincalendarv2.widget.glance

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview.previewEvents
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.GD
import java.util.*

@Composable
fun WidgetBodyDefault(events: List<EventModel>, model: WidgetModel) {
    val dividerColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
    val backColor = model.backgroundColor.toColor()
    val opacity = model.opacity
    val textColorStyle = TextStyle(
        fontSize = (WidgetModel.INITIAL_FONT_SIZE + model.textSizeDelta).sp,
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
                Column() {

                    Column(
                        modifier = GlanceModifier
                            .clickable(actionStartActivity(createOpenEventIntent(event.eventId)))
                            .padding(horizontal = GD.eventItemPaddingH, vertical = GD.eventItemPaddingV),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = event.dateStart.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.getDefault())
                                .replaceFirstChar { it.uppercase() },
                            style = textColorStyle
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
                            Text(text = event.title, style = textColorStyle, maxLines = 1)
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

@Preview
@Composable
private fun Preview_WidgetBody() {
    WidgetBodyDefault(previewEvents, DummyWidget)
}
