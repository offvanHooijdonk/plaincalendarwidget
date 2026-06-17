package by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.settings.preview.EventColorMark
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.PlainTheme
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.LabeledCheckBox
import by.offvanhooijdonk.plaincalendarv2.widget.ui.views.Spinner
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.calendar.configure.ConfigureViewModel

@Composable
fun SettingsScreen(calendarWidgetModel: CalendarWidgetModel, onAction: (ConfigureViewModel.Action) -> Unit) {
    CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelLarge) {

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
                isChecked = calendarWidgetModel.showDateAsTextLabel,
                onCheck = { onAction(ConfigureViewModel.Action.OnDateAsTextPick) },
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val options = CalendarWidgetModel.ShowEndDate.entries.map { it.title }
                val selectedOption = remember(calendarWidgetModel.showEndDate) { mutableIntStateOf(calendarWidgetModel.showEndDate.ordinal) }
                Spacer(modifier = Modifier.width(dimens().spacingM))
                Text(stringResource(R.string.settings_show_end_date))
                Spacer(modifier = Modifier.width(dimens().spacingS))
                Spinner(
                    text = options[selectedOption.intValue],
                    items = options,
                    onItemSelected = {
                        selectedOption.intValue = it
                        onAction(ConfigureViewModel.Action.OnShowEndDatePick(CalendarWidgetModel.ShowEndDate.entries[it]))
                    }
                )
            }

            Column {
                LabeledCheckBox(
                    labelText = stringResource(R.string.settings_show_end_color),
                    isChecked = calendarWidgetModel.showEventColor,
                    onCheck = { onAction(ConfigureViewModel.Action.OnShowEventColorPick) },
                )
                AnimatedVisibility(visible = calendarWidgetModel.showEventColor) {
                    Column {
                        Spacer(modifier = Modifier.height(dimens().spacingM))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SelectableShape(
                                isSelected = calendarWidgetModel.eventColorShape == CalendarWidgetModel.EventColorShape.CIRCLE,
                                onClick = { onAction(ConfigureViewModel.Action.OnEventColorShapePick(CalendarWidgetModel.EventColorShape.CIRCLE)) },
                            ) {
                                EventColorMark(MaterialTheme.colorScheme.secondary, CalendarWidgetModel.EventColorShape.CIRCLE, multiplier = 4.0f)
                            }
                            Spacer(modifier = Modifier.width(dimens().eventMarkSpacing))
                            SelectableShape(
                                isSelected = calendarWidgetModel.eventColorShape == CalendarWidgetModel.EventColorShape.SQUARE,
                                onClick = { onAction(ConfigureViewModel.Action.OnEventColorShapePick(CalendarWidgetModel.EventColorShape.SQUARE)) },
                            ) {
                                EventColorMark(MaterialTheme.colorScheme.secondary, CalendarWidgetModel.EventColorShape.SQUARE, multiplier = 4.0f)
                            }
                        }
                    }
                }
            }

            LabeledCheckBox(
                labelText = stringResource(R.string.settings_show_events_dividers),
                isChecked = calendarWidgetModel.showEventDividers,
                onCheck = { onAction(ConfigureViewModel.Action.OnShowDividersPick) },
            )
        }
    }
}

@Composable
private fun SelectableShape(isSelected: Boolean, onClick: () -> Unit, block: @Composable () -> Unit) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.surface) { // for ripple color
        Box(
            modifier = Modifier
                .clickable(enabled = true, onClick = onClick)
                .let {
                    if (isSelected) {
                        it.then(
                            Modifier.background(
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.12f),
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
        SettingsScreen(calendarWidgetModel = DummyWidget, onAction = {})
    }
}
