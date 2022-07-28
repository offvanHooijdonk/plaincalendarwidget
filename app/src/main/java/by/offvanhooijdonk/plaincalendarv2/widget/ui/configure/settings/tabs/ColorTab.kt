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
fun ColorTab(colorSelected: Color, onColorPick: (Color) -> Unit) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = Unit) {
        listState.scrollToItem(BackgroundColors.indexOf(colorSelected).let { if (it == -1) 0 else it }, 0)
    }
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = BackgroundColors, key = { it.value.toLong() }) { colorItem ->
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
                            painter = painterResource(id = R.drawable.ic_round_check_circle_24),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

private val BackgroundColors = listOf(
    Color.Blue,
    Color.Gray,
    Color.Cyan,
    Color.Black,
    Color.DarkGray,
    Color.LightGray,
    Color.Red,
    Color.Green,
    Color.Yellow,
    Color(0xFF6750A4),
    Color(0xFFB58392),
    Color(0xFF958DA5),
    Color.White,
    Color(0xFF939094),
)

@Preview
@Composable
private fun Preview_ColorsTab() {
    ColorTab(Color.White) {}
}
