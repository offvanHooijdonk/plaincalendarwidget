package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.R

@Composable
fun ColorTab() {
    val selectedItem = remember {
        mutableStateOf<Color?>(null)
    }
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = BackgroundColors, key = { it.value.toLong() }) { colorItem ->
            Box(modifier = Modifier.padding(end = 8.dp)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(colorItem, RoundedCornerShape(2.dp))
                        .border(width = 1.dp, color = Color.Gray, RoundedCornerShape(2.dp)).padding(4.dp)
                        .clickable { selectedItem.value = colorItem },
                    contentAlignment = Alignment.Center,
                ) {
                    if (colorItem == selectedItem.value) {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_check_circle_24),
                            contentDescription = null,
                            tint = Color.White,
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
    Color.Yellow,
    Color.White,
)

@Preview
@Composable
private fun Preview_ColorsTab() {
    ColorTab()
}
