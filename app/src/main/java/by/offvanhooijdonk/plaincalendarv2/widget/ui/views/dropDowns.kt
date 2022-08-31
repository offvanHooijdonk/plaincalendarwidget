package by.offvanhooijdonk.plaincalendarv2.widget.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel

@Composable
fun Spinner(text: String, items: List<String>, onItemSelected: (Int) -> Unit) {
    val isExpanded = remember { mutableStateOf(false) }

    Box {
        Text(
            modifier = Modifier
                .clickable { isExpanded.value = true }
                .background(MaterialTheme.colors.secondary.copy(0.3f), shape = MaterialTheme.shapes.small)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        DropdownMenu(expanded = isExpanded.value, onDismissRequest = { isExpanded.value = false }) {
            WidgetModel.ShowEndDate.values().forEachIndexed { index, item ->
                SpinnerItem(label = item.title) { onItemSelected(index); isExpanded.value = false }
            }
        }
    }
}

@Composable
private fun SpinnerItem(label: String, onClick: () -> Unit) {
    DropdownMenuItem(onClick = onClick) {
        Text(text = label)
    }
}
