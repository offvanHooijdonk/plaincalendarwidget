package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview.EventColorMark
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.LabeledCheckBox
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.Spinner

@Composable
fun SettingsScreen(widgetModel: WidgetModel, onChange: (WidgetModel) -> Unit) {
    Column(modifier = Modifier.padding(top = D.dialogCornerRadius, start = D.spacingL, end = D.spacingXL, bottom = D.spacingXL)) {
        LabeledCheckBox(
            labelText = stringResource(R.string.settings_date_as_text),
            isChecked = widgetModel.showDateAsTextLabel,
            onCheck = { onChange(widgetModel.copy(showDateAsTextLabel = !widgetModel.showDateAsTextLabel)) },
        )
        Spacer(modifier = Modifier.height(D.spacingM))

        Row(verticalAlignment = Alignment.CenterVertically) {
            val options = WidgetModel.ShowEndDate.values().map { it.title }
            val selectedOption = remember(widgetModel.showEndDate) { mutableStateOf(widgetModel.showEndDate.ordinal) }
            Spacer(modifier = Modifier.width(D.spacingM))
            Text(stringResource(R.string.settings_show_end_date))
            Spacer(modifier = Modifier.width(D.spacingS))
            Spinner(
                text = options[selectedOption.value],
                items = options,
                onItemSelected = {
                    selectedOption.value = it; onChange(widgetModel.copy(showEndDate = WidgetModel.ShowEndDate.values()[it]))
                }
            )
        }
        Spacer(modifier = Modifier.height(D.spacingM))

        LabeledCheckBox(
            labelText = stringResource(R.string.settings_show_end_color),
            isChecked = widgetModel.showEventColor,
            onCheck = { onChange(widgetModel.copy(showEventColor = !widgetModel.showEventColor)) },
        )
        AnimatedVisibility(visible = widgetModel.showEventColor) {
            Column {
                Spacer(modifier = Modifier.height(D.spacingM))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SelectableShape(
                        isSelected = widgetModel.eventColorShape == WidgetModel.EventColorShape.CIRCLE,
                        onClick = { onChange(widgetModel.copy(eventColorShape = WidgetModel.EventColorShape.CIRCLE)) }) {
                        EventColorMark(MaterialTheme.colors.secondary, WidgetModel.EventColorShape.CIRCLE, multiplier = 4.0f)
                    }
                    SelectableShape(
                        isSelected = widgetModel.eventColorShape == WidgetModel.EventColorShape.SQUARE,
                        onClick = { onChange(widgetModel.copy(eventColorShape = WidgetModel.EventColorShape.SQUARE)) }) {
                        EventColorMark(MaterialTheme.colors.secondary, WidgetModel.EventColorShape.SQUARE, multiplier = 4.0f)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(D.spacingM))

        LabeledCheckBox(
            labelText = stringResource(R.string.settings_show_events_dividers),
            isChecked = widgetModel.showEventDividers,
            onCheck = { onChange(widgetModel.copy(showEventDividers = !widgetModel.showEventDividers)) },
        )
    }
}

@Composable
private fun SelectableShape(isSelected: Boolean, onClick: () -> Unit, block: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.surface) { // for ripple color
        Box(
            modifier = Modifier
                .clickable(enabled = true, onClick = onClick)
                .let {
                    if (isSelected) {
                        it.then(
                            Modifier.background(
                                color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(D.spacingM),
                            )
                        )
                    } else {
                        it
                    }
                }
                .padding(vertical = D.spacingSM, horizontal = D.spacingM)
        ) {
            block()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_SettingsScreen() {
    PlainTheme {
        SettingsScreen(widgetModel = DummyWidget, onChange = {})
    }
}
