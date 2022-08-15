package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = value.toString(), fontSize = 24.sp)
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedButton(onClick = { onChange(value - 1) }) {
            Text(text = "-")
        }
    }
}
