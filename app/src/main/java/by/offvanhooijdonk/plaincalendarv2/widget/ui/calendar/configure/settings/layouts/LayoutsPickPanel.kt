package by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.layouts

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.calendar.CalendarWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroLayouts
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroStyleLayouts
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.intro.IntroTargets
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.IntroShowCaseScope

@Composable
fun IntroShowCaseScope.LayoutsPickPanel(
    modifier: Modifier = Modifier,
    widget: CalendarWidgetModel,
    onLayoutPick: (CalendarWidgetModel.LayoutType) -> Unit
) {
    Box(modifier = Modifier.then(modifier)) {
        val selected = remember(widget) { widget.layoutType.ordinal }
        ScrollableTabRow(
            selectedTabIndex = selected,
            edgePadding = dimens().spacingL,
            indicator = {},
            containerColor = MaterialTheme.colorScheme.background
        ) {
            val textAlpha = 0.5f
            val primaryColor = MaterialTheme.colorScheme.primary
            val unselectedTextColor = remember(LocalContentColor.current) { primaryColor.copy(alpha = textAlpha) }

            LayoutsList.forEachIndexed { index, layoutItem ->
                val backColor = if (index == selected) MaterialTheme.colorScheme.primary else Color.Transparent
                Box(modifier = Modifier.padding(vertical = dimens().spacingM)) {
                    Tab(
                        modifier = Modifier
                            .background(backColor, RoundedCornerShape(dimens().spacingM)).run {
                                if (index == selected)
                                    introShowCaseTarget(IntroTargets.LAYOUTS.ordinal, IntroStyleLayouts) { IntroLayouts() }
                                else this
                            },
                        selected = index == selected,
                        onClick = { onLayoutPick(CalendarWidgetModel.LayoutType.entries.getOrNull(index) ?: CalendarWidgetModel.LayoutType.default) },
                        selectedContentColor = MaterialTheme.colorScheme.background,
                        unselectedContentColor = unselectedTextColor,
                        text = {
                            Text(text = stringResource(layoutItem.titleRes))
                        }
                    )
                }
            }
        }
    }
}

private data class LayoutItem(
    @field:StringRes val titleRes: Int,
    val type: CalendarWidgetModel.LayoutType,
)

private val LayoutsList = listOf(
    LayoutItem(R.string.layout_title_timeline, CalendarWidgetModel.LayoutType.TIMELINE),
    LayoutItem(R.string.layout_title_per_day, CalendarWidgetModel.LayoutType.PER_DAY),
)

@Preview
@Composable
private fun Preview_LayoutsPickPanel() {
    IntroShowCaseScaffold(showIntroShowCase = false, onShowCaseCompleted = {}) {
        LayoutsPickPanel(widget = DummyWidget) {}
    }
}
