package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.systemGestureExclusion
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens

@Composable
fun OpacityTab(opacitySelected: Float, onPickOpacity: (Float) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = dimens().spacingL, vertical = dimens().spacingM)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val sliderValue = remember(opacitySelected) { mutableStateOf(opacitySelected * 100 / OPACITY_VALUE_STEP) }
        Text(
            modifier = Modifier.width(dimens().spacingXXL),
            text = (sliderValue.value.toInt() * OPACITY_VALUE_STEP).toString(),
            color = MaterialTheme.colors.onSurface,
            fontSize = 18.sp,
            textAlign = TextAlign.End,
        )
        Spacer(modifier = Modifier.width(dimens().spacingL))
        Slider(
            modifier = Modifier.systemGestureExclusion(),
            value = sliderValue.value,
            valueRange = OPACITY_RANGE_MIN.toFloat()..OPACITY_RANGE_MAX.toFloat(),
            steps = OPACITY_RANGE_STEPS,
            onValueChange = {
                sliderValue.value = it
                onPickOpacity(it / 100 * OPACITY_VALUE_STEP)
            },
        )
    }
}

private const val OPACITY_VALUE_STEP = 5
private const val OPACITY_RANGE_MIN = 0
private const val OPACITY_RANGE_MAX = 100 / OPACITY_VALUE_STEP
private const val OPACITY_RANGE_STEPS = OPACITY_RANGE_MAX - OPACITY_RANGE_MIN - 1

@Preview
@Composable
private fun Preview_OpacityTab() {
    OpacityTab(0.5f) {}
}
