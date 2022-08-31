@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts.LayoutType
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: WidgetModel) {
    Box(
        modifier = Modifier
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(targetState = widget.layoutType) { layout ->
            when (layout) {
                LayoutType.DEFAULT -> WidgetBlueprint(widget)
                LayoutType.EXTENDED -> Text(text = "Nothing to show yet")//WidgetBlueprint(widget, eventPreviewColor)
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
        shape = RoundedCornerShape(28.dp),
    ) {
        Box(modifier = Modifier.padding(vertical = 8.dp)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
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
private fun WidgetEventItem(event: EventModel, widgetModel: WidgetModel) {// replace widget with raw style values
    Surface(
        modifier = Modifier.padding(2.dp),
        onClick = { /**/ },
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent
    ) {
        val textColor = widgetModel.textColor.toColor()
        val textSize = (14 + widgetModel.textSizeDelta).sp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(top = 4.dp, bottom = 8.dp)
        ) {
            Text(
                text = event.dateStart.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault()),
                color = textColor,
                fontSize = textSize,
            )

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                AnimatedContent(targetState = widgetModel.showEventColor) { showColor ->
                    when (showColor) {
                        true -> Row {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                painter = painterResource(R.drawable.ic_circle),
                                tint = widgetModel.calendars.firstOrNull()?.color?.let { Color(it.toLong()) } ?: Color.Blue,
                                contentDescription = null,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        false -> Unit
                    }
                }
                Text(text = event.title, color = textColor, fontSize = textSize, maxLines = 1)
            }
        }
    }
}

@Preview
@Composable
private fun Preview_WidgetBlueprint() {
    MaterialTheme {
        WidgetBlueprint(DummyWidget.copy(opacity = 0.5f))
    }
}

val previewEvents = listOf(
    EventModel(
        1,
        "Bicycling on every Wednesday evening",
        LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.WEDNESDAY.value.toLong()),
        LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.THURSDAY.value.toLong()),
    ),
    EventModel(
        2,
        "Go waltzing to the zoo",
        LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.THURSDAY.value.toLong()),
        LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.FRIDAY.value.toLong()),
    ),
    EventModel(
        3,
        "Lazing on a Sunday afternoon",
        LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.SUNDAY.value.toLong()),
        LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, DayOfWeek.MONDAY.value.toLong()),
    ),
)
