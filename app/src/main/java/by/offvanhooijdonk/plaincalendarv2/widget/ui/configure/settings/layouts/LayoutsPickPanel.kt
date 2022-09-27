package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.intro.IntroLayouts
import by.offvanhooijdonk.plaincalendarv2.widget.ui.intro.IntroStyleLayouts
import by.offvanhooijdonk.plaincalendarv2.widget.ui.intro.IntroTargets
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.IntroShowCaseScope

@Composable
fun IntroShowCaseScope.LayoutsPickPanel(
    modifier: Modifier = Modifier,
    widget: WidgetModel,
    onLayoutPick: (WidgetModel.LayoutType) -> Unit
) {
    Box(modifier = Modifier.then(modifier)) {
        val selected = remember(widget) { widget.layoutType.ordinal }
        ScrollableTabRow(
            selectedTabIndex = selected,
            edgePadding = D.spacingL,
            indicator = {},
            backgroundColor = MaterialTheme.colors.background
        ) {
            val textAlpha = ContentAlpha.medium
            val primaryColor = MaterialTheme.colors.primary
            val unselectedTextColor = remember(LocalContentColor.current) { primaryColor.copy(alpha = textAlpha) }

            LayoutsList.forEachIndexed { index, layoutItem ->
                val backColor = if (index == selected) MaterialTheme.colors.primary else Color.Transparent
                Box(modifier = Modifier.padding(vertical = D.spacingM)) {
                    Tab(
                        modifier = Modifier
                            .background(backColor, RoundedCornerShape(D.spacingM)).run {
                                if (index == selected)
                                    introShowCaseTarget(IntroTargets.LAYOUTS.ordinal, IntroStyleLayouts) { IntroLayouts() }
                                else this
                            },
                        selected = index == selected,
                        onClick = { onLayoutPick(WidgetModel.LayoutType.values().getOrNull(index) ?: WidgetModel.LayoutType.default) },
                        selectedContentColor = MaterialTheme.colors.background,
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
    @StringRes val titleRes: Int,
    val type: WidgetModel.LayoutType,
)

private val LayoutsList = listOf(
    LayoutItem(R.string.layout_title_timeline, WidgetModel.LayoutType.TIMELINE),
    LayoutItem(R.string.layout_title_per_day, WidgetModel.LayoutType.PER_DAY),
)

@Preview
@Composable
private fun Preview_LayoutsPickPanel() {
    IntroShowCaseScaffold(showIntroShowCase = false, onShowCaseCompleted = {}) {
        LayoutsPickPanel(widget = DummyWidget) {}
    }
}
