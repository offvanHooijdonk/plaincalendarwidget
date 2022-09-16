package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.LabeledCheckBox
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.Spinner

@Composable
fun SettingsScreen(widgetModel: WidgetModel, onChange: (WidgetModel) -> Unit) {
    Column(modifier = Modifier.padding(top = 28.dp, start = 16.dp, end = 24.dp, bottom = 24.dp)) {
        LabeledCheckBox(
            label = stringResource(R.string.settings_date_as_text),
            isChecked = widgetModel.showDateAsTextLabel,
            onCheck = { onChange(widgetModel.copy(showDateAsTextLabel = !widgetModel.showDateAsTextLabel)) },
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val options = WidgetModel.ShowEndDate.values().map { it.title }
            val selectedOption = remember(widgetModel.showEndDate) { mutableStateOf(widgetModel.showEndDate.ordinal) }
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.settings_show_end_date))
            Spacer(modifier = Modifier.width(4.dp))
            Spinner(
                text = options[selectedOption.value],
                items = options,
                onItemSelected = {
                    selectedOption.value = it; onChange(widgetModel.copy(showEndDate = WidgetModel.ShowEndDate.values()[it]))
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        LabeledCheckBox(
            label = stringResource(R.string.settings_show_end_color),
            isChecked = widgetModel.showEventColor,
            onCheck = { onChange(widgetModel.copy(showEventColor = !widgetModel.showEventColor)) },
        )
        Spacer(modifier = Modifier.height(8.dp))

        LabeledCheckBox(
            label = stringResource(R.string.settings_show_events_dividers),
            isChecked = widgetModel.showEventDividers,
            onCheck = { onChange(widgetModel.copy(showEventDividers = !widgetModel.showEventDividers)) },
        )
        /*Spacer(modifier = Modifier.height(8.dp))*/
    }
}

@Preview
@Composable
private fun Preview_SettingsScreen() {
    PlainTheme {
        SettingsScreen(widgetModel = DummyWidget, onChange = {})
    }
}
