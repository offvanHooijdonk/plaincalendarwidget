package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.layouts

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.R

@Composable
fun LayoutsPickPanel(modifier: Modifier = Modifier, widget: WidgetModel, onLayoutPick: (LayoutType) -> Unit) {
    Box(modifier = Modifier.then(modifier)) {
        val selected = remember(widget) { widget.layoutType.ordinal }
        ScrollableTabRow(
            selectedTabIndex = selected,
            edgePadding = 16.dp,
            indicator = {},
            backgroundColor = MaterialTheme.colors.background
        ) {
            val textAlpha = ContentAlpha.medium
            val primaryColor = MaterialTheme.colors.primary
            val unselectedTextColor = remember(LocalContentColor.current) { primaryColor.copy(alpha = textAlpha) }

            LayoutsList.forEachIndexed { index, layoutItem ->
                val backColor = if (index == selected) MaterialTheme.colors.primary else Color.Transparent
                Box(modifier = Modifier.padding(vertical = 8.dp)) {
                    Tab(
                        modifier = Modifier.background(backColor, RoundedCornerShape(8.dp)),
                        selected = index == selected,
                        onClick = { onLayoutPick(LayoutType.values().getOrNull(index) ?: LayoutType.default) },
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
    val type: LayoutType,
)

private val LayoutsList = listOf(
    LayoutItem(R.string.layout_title_default, LayoutType.DEFAULT),
    LayoutItem(R.string.layout_title_extended, LayoutType.EXTENDED),
)

enum class LayoutType {
    DEFAULT, EXTENDED;

    companion object {
        val default = DEFAULT
    }
}

@Preview
@Composable
private fun Preview_LayoutsPickPanel() {
    LayoutsPickPanel(widget = WidgetModel.createDefault()) {}
}
