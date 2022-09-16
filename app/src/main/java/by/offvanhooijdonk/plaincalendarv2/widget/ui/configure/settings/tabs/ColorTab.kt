package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D

@Composable
fun BackgroundColorTab(colorSelected: Color, onColorPick: (Color) -> Unit) {
    ColorTab(BackgroundColors, colorSelected, onColorPick)
}

@Composable
fun TextColorTab(colorSelected: Color, onColorPick: (Color) -> Unit) {
    ColorTab(TextColors, colorSelected, onColorPick)
}

@Composable
private fun ColorTab(colorsList: List<Color>, colorSelected: Color, onColorPick: (Color) -> Unit) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = Unit) {
        listState.scrollToItem(colorsList.indexOf(colorSelected).let { if (it == -1) 0 else it }, 0)
    }
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = D.spacingL, vertical = D.spacingM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = colorsList, key = { it.value.toLong() }) { colorItem ->
            Box(modifier = Modifier.padding(end = D.spacingM)) {
                Box(
                    modifier = Modifier
                        .size(D.spacingXXL)
                        .background(colorItem, RoundedCornerShape(D.spacingXS))
                        .border(
                            width = if (colorItem == colorSelected) D.spacingXS else D.spacingXXS,
                            color = MaterialTheme.colors.primary,
                            RoundedCornerShape(D.spacingXS)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) { onColorPick(colorItem) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (colorItem == colorSelected) {
                        Icon(
                            modifier = Modifier.padding(D.spacingXS),
                            painter = painterResource(id = R.drawable.ic_check),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview_ColorsTab() {
    BackgroundColorTab(Color.White) {}
}
