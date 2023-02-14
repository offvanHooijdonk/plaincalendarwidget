package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens

@Composable
fun CalendarsPickDialog(
    pickedCalendars: List<CalendarModel>,
    allCalendars: List<CalendarModel>,
    onDismissRequest: () -> Unit,
    onSelectionSave: (List<CalendarModel>) -> Unit
) {
    val selection = remember(pickedCalendars) { pickedCalendars.toMutableList() }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Box(modifier = Modifier.padding(top = dimens().spacingL)) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(items = allCalendars, key = { it.id }) { calendar ->
                        val isChecked = remember { mutableStateOf(selection.contains(calendar)) }

                        CalendarsListItem(calendar = calendar, isChecked = isChecked.value) {
                            isChecked.value = !isChecked.value
                            selection.apply { if (isChecked.value) add(calendar) else remove(calendar) }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onSelectionSave(selection) }) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(android.R.string.cancel))
            }
        },
    )
}

@Composable
private fun CalendarsListItem(calendar: CalendarModel, isChecked: Boolean, onItemClick: () -> Unit) {
    val calendarColor = calendar.color?.let { Color(it.toLong()) } ?: MaterialTheme.colors.secondary
    Box(modifier = Modifier.clickable { onItemClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens().spacingM, vertical = dimens().spacingSM),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked, onCheckedChange = null,
                colors = CheckboxDefaults.colors(uncheckedColor = calendarColor, checkedColor = calendarColor)
            )
            Spacer(modifier = Modifier.width(dimens().spacingML))
            if (!calendar.isPrimaryOnAccount) Spacer(modifier = Modifier.width(dimens().spacingXS))

            Text(text = calendar.displayName, fontWeight = if (calendar.isPrimaryOnAccount) FontWeight.Medium else FontWeight.Normal)
        }
    }
}
