package by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun BackgroundColorTab(colorSelected: Color, onColorPick: (Color) -> Unit) {
    val dynamicColors = dynamicLightColorScheme(LocalContext.current).let { scheme ->
        listOf(
            scheme.primary,
            scheme.secondary,
            scheme.tertiary,
            scheme.onPrimary,
            scheme.onSecondary,
            scheme.onTertiary,
            scheme.primaryContainer,
            scheme.secondaryContainer,
            scheme.tertiaryContainer,
            scheme.onPrimaryContainer,
            scheme.onSecondaryContainer,
            scheme.onTertiaryContainer,
        ).distinctBy { it.value }
    }
    ColorTab(dynamicColors + BackgroundColors, colorSelected, onColorPick)
}

@Composable
fun TextColorTab(colorSelected: Color, onColorPick: (Color) -> Unit) {
    ColorTab(TextColors, colorSelected, onColorPick)
}

@Composable
private fun ColorTab(colorsList: List<Color>, colorSelected: Color, onColorPick: (Color) -> Unit) {
    val composableScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val colorSelectionDark = Color.Gray.copy(alpha = SelectionMarkDarkAlpha)
    val colorSelectionLight = Color.White.copy(alpha = SelectionMarkLightAlpha)

    LaunchedEffect(key1 = colorSelected) {
        composableScope.launch {
            val colorIndex = colorsList.indexOf(colorSelected)
            if ((listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) < colorIndex) {
                listState.scrollToItem(colorIndex.let { if (it == -1) 0 else it }, 0)
            }
        }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        contentPadding = PaddingValues(horizontal = dimens().spacingL, vertical = dimens().spacingM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = colorsList, key = { it.value.toLong() }) { colorItem ->
            IconButton(onClick = { onColorPick(colorItem) }) {
                Box(
                    modifier = Modifier
                        .size(dimens().spacingXXL)
                        .background(colorItem, RoundedCornerShape(dimens().spacingXS))
                        .border(
                            width = dimens().spacingXXS,
                            color = Color.Gray,
                            RoundedCornerShape(dimens().spacingXS)
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (colorItem == colorSelected) {
                        val selectionColor = remember(colorItem) {
                            if (colorItem.luminance() > 0.7f) colorSelectionDark else colorSelectionLight
                        }
                        Icon(
                            modifier = Modifier.padding(dimens().spacingXS),
                            painter = painterResource(id = R.drawable.ic_check),
                            tint = selectionColor,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

private const val SelectionMarkDarkAlpha = 0.9f
private const val SelectionMarkLightAlpha = 0.95f

@Preview
@Composable
private fun Preview_ColorsTab_LightSelected() {
    BackgroundColorTab(BackgroundColors[0]) {}
}

@Preview
@Composable
private fun Preview_ColorsTab_DarkSelected() {
    BackgroundColorTab(BackgroundColors[1]) {}
}

@Preview
@Composable
private fun Preview_ColorsTab_SemiLightSelected() {
    BackgroundColorTab(BackgroundColors[3]) {}
}
