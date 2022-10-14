package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.glance.GlanceId
import by.offvanhooijdonk.plaincalendarv2.widget.R

@Composable
fun WidgetsDropDown(idsList: List<GlanceId>, onPick: (GlanceId) -> Unit) {
    val isExpanded = remember { mutableStateOf(false) }

    IconButton(onClick = { isExpanded.value = !isExpanded.value }) {
        Icon(painter = painterResource(R.drawable.ic_arrow_drop_down), contentDescription = null)
    }

    DropdownMenu(expanded = isExpanded.value, onDismissRequest = { isExpanded.value = false }) {
        idsList.forEach { id ->
            DropdownMenuItem(onClick = { onPick(id); isExpanded.value = false }) {
                Text(text = "Widget #${id.toIntId()}")
            }
        }
    }
}