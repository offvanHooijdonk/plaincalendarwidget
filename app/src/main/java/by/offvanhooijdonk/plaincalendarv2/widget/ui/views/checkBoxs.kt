package by.offvanhooijdonk.plaincalendarv2.widget.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens

@Composable
fun LabeledCheckBox(modifier: Modifier = Modifier, isChecked: Boolean, onCheck: () -> Unit, label: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onCheck() }
            .padding(horizontal = dimens().spacingM, vertical = dimens().spacingS)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(modifier = Modifier.width(dimens().spacingS))
        label()
    }
}

@Composable
fun LabeledCheckBox(modifier: Modifier = Modifier, labelText: String, isChecked: Boolean, onCheck: () -> Unit) {
    LabeledCheckBox(modifier, isChecked, onCheck) {
        Text(text = labelText)
    }
}
