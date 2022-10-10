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
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D

@Composable
fun LabeledCheckBox(modifier: Modifier = Modifier, isChecked: Boolean, onCheck: () -> Unit, label: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onCheck() }
            .padding(horizontal = D.spacingM, vertical = D.spacingS)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(modifier = Modifier.width(D.spacingS))
        label()
    }
}

@Composable
fun LabeledCheckBox(modifier: Modifier = Modifier, labelText: String, isChecked: Boolean, onCheck: () -> Unit) {
    LabeledCheckBox(modifier, isChecked, onCheck) {
        Text(text = labelText)
    }
}
