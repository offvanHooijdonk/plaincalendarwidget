@file:OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: WidgetModel) {
    Box(
        modifier = Modifier.then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(targetState = widget.layoutType) { layout ->
            when (layout) {
                WidgetModel.LayoutType.TIMELINE -> WidgetBlueprintTimeline(widget)
                WidgetModel.LayoutType.PER_DAY -> WidgetBlueprintDaily(widget)
            }
        }
    }
}
