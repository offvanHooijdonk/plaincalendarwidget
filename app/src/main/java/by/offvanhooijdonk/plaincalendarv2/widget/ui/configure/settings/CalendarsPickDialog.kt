package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel

@Composable
fun CalendarsPickDialog(
    pickedCalendars: List<CalendarModel>,
    allCalendars: List<CalendarModel>,
    onDismissRequest: () -> Unit,
    onSelectionSave: (List<CalendarModel>) -> Unit
) {
    val selection = remember(pickedCalendars) { pickedCalendars.toMutableList() }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(shape = RoundedCornerShape(28.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(items = allCalendars, key = { it.id }) { calendar ->
                        val isChecked = remember { mutableStateOf(pickedCalendars.contains(calendar)) }
                        fun onSelection(isSelected: Boolean, calendar: CalendarModel) {
                            isChecked.value = isSelected
                            selection.apply { if (isSelected) add(calendar) else remove(calendar) }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelection(!isChecked.value, calendar) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked.value, onCheckedChange = { onSelection(!isChecked.value, calendar) },
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = calendar.displayName)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = { onSelectionSave(selection) }) {
                        Text(text = stringResource(android.R.string.ok))
                    }
                }
            }
        }
    }
}
