package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.LabeledCheckBox

@Composable
fun TextSizeTab(value: Int, isBold: Boolean, onSizeChange: (Int) -> Unit, onStyleChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = D.spacingL),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = { onSizeChange(value + 1) }) {
                Text(text = "+")
            }
            Spacer(modifier = Modifier.width(D.spacingL))
            Text(text = value.toString(), color = MaterialTheme.colors.onSurface, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(D.spacingL))
            OutlinedButton(onClick = { onSizeChange(value - 1) }) {
                Text(text = "-")
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            LabeledCheckBox(
                labelText = stringResource(R.string.text_style_bold),
                isChecked = isBold,
                onCheck = { onStyleChange(!isBold) })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview_TextSizeTab() {
    PlainTheme {
        TextSizeTab(value = 15, true, onSizeChange = {}, onStyleChange = {})
    }
}
