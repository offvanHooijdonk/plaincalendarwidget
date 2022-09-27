package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D

@Composable
fun TextSizeTab(value: Int, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = { onChange(value + 1) }) {
            Text(text = "+")
        }
        Spacer(modifier = Modifier.width(D.spacingL))
        Text(text = value.toString(), fontSize = 24.sp)
        Spacer(modifier = Modifier.width(D.spacingL))
        OutlinedButton(onClick = { onChange(value - 1) }) {
            Text(text = "-")
        }
    }
}
