package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.R

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
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = colorsList, key = { it.value.toLong() }) { colorItem ->
            Box(modifier = Modifier.padding(end = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(colorItem, RoundedCornerShape(2.dp))
                        .border(
                            width = if (colorItem == colorSelected) 2.dp else 1.dp,
                            color = MaterialTheme.colors.primary,
                            RoundedCornerShape(2.dp)
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) { onColorPick(colorItem) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (colorItem == colorSelected) {
                        Icon(
                            modifier = Modifier.padding(2.dp),
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
