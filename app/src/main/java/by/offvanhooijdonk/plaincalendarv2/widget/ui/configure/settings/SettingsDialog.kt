package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.ConfigureViewModel.Action
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview.EventColorMark
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.LabeledCheckBox
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.Spinner

@Composable
fun SettingsScreen(widgetModel: WidgetModel, onAction: (Action) -> Unit) {
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.subtitle1) {

        Column(
            modifier = Modifier.padding(
                top = dimens().dialogCornerRadius,
                start = dimens().spacingL,
                end = dimens().spacingXL,
                bottom = dimens().spacingXL
            ),
            verticalArrangement = Arrangement.spacedBy(dimens().spacingXL),
        ) {
            LabeledCheckBox(
                labelText = stringResource(R.string.settings_date_as_text),
                isChecked = widgetModel.showDateAsTextLabel,
                onCheck = { onAction(Action.OnDateAsTextPick) },
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val options = WidgetModel.ShowEndDate.entries.map { it.title }
                val selectedOption = remember(widgetModel.showEndDate) { mutableIntStateOf(widgetModel.showEndDate.ordinal) }
                Spacer(modifier = Modifier.width(dimens().spacingM))
                Text(stringResource(R.string.settings_show_end_date))
                Spacer(modifier = Modifier.width(dimens().spacingS))
                Spinner(
                    text = options[selectedOption.intValue],
                    items = options,
                    onItemSelected = {
                        selectedOption.intValue = it
                        onAction(Action.OnShowEndDatePick(WidgetModel.ShowEndDate.entries[it]))
                    }
                )
            }

            Column {
                LabeledCheckBox(
                    labelText = stringResource(R.string.settings_show_end_color),
                    isChecked = widgetModel.showEventColor,
                    onCheck = { onAction(Action.OnShowEventColorPick) },
                )
                AnimatedVisibility(visible = widgetModel.showEventColor) {
                    Column {
                        Spacer(modifier = Modifier.height(dimens().spacingM))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SelectableShape(
                                isSelected = widgetModel.eventColorShape == WidgetModel.EventColorShape.CIRCLE,
                                onClick = { onAction(Action.OnEventColorShapePick(WidgetModel.EventColorShape.CIRCLE)) },
                            ) {
                                EventColorMark(MaterialTheme.colors.secondary, WidgetModel.EventColorShape.CIRCLE, multiplier = 4.0f)
                            }
                            Spacer(modifier = Modifier.width(dimens().eventMarkSpacing))
                            SelectableShape(
                                isSelected = widgetModel.eventColorShape == WidgetModel.EventColorShape.SQUARE,
                                onClick = { onAction(Action.OnEventColorShapePick(WidgetModel.EventColorShape.SQUARE)) },
                            ) {
                                EventColorMark(MaterialTheme.colors.secondary, WidgetModel.EventColorShape.SQUARE, multiplier = 4.0f)
                            }
                        }
                    }
                }
            }

            LabeledCheckBox(
                labelText = stringResource(R.string.settings_show_events_dividers),
                isChecked = widgetModel.showEventDividers,
                onCheck = { onAction(Action.OnShowDividersPick) },
            )
        }
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
                                shape = RoundedCornerShape(dimens().spacingM),
                            )
                        )
                    } else {
                        it
                    }
                }
                .padding(vertical = dimens().spacingSM, horizontal = dimens().spacingM)
        ) {
            block()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_SettingsScreen() {
    PlainTheme {
        SettingsScreen(widgetModel = DummyWidget, onAction = {})
    }
}
