package by.offvanhooijdonk.plaincalendarv2.widget.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LabeledCheckBox(label: String, isChecked: Boolean, onCheck: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onCheck() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label)
    }
}
