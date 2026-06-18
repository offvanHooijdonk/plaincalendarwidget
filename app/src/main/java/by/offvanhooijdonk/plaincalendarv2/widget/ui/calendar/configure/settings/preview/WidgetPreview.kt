@file:OptIn(ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.CalendarWidgetModel

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: CalendarWidgetModel) {
    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(targetState = widget.layoutType) { layout ->
            when (layout) {
                CalendarWidgetModel.LayoutType.TIMELINE -> WidgetBlueprintTimeline(widget)
                CalendarWidgetModel.LayoutType.PER_DAY -> WidgetBlueprintPerDay(widget)
            }
        }
    }
}
