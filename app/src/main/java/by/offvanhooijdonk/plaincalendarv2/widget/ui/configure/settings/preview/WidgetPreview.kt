@file:OptIn(ExperimentalMaterialApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendar.widget.model.EventModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.R
import java.util.*

@Composable
fun WidgetPreview(modifier: Modifier = Modifier, widget: WidgetModel) {
    Box(
        modifier = Modifier
            .then(modifier)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        WidgetBlueprint(widget)
    }
}

@Composable
private fun WidgetBlueprint(widget: WidgetModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color(widget.backgroundColor.toULong()).copy(alpha = widget.opacity),
        shape = RoundedCornerShape(28.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent, RoundedCornerShape(12.dp)),
            contentPadding = PaddingValues(all = 4.dp)
        ) {
            items(previewEvents, key = { it.id }) {
                Column {
                    WidgetEventItem(it)
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun WidgetEventItem(event: EventModel) {
    Surface(
        modifier = Modifier.padding(2.dp),
        onClick = { /**/ },
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 4.dp, bottom = 8.dp)
        ) {
            Text(text = Calendar.getInstance().apply { time = event.dateStart }
                .getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) ?: "Soon")

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(R.drawable.ic_circle),
                    tint = Color.Blue, // todo widget color
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = event.title)
            }
        }
    }
}

@Preview
@Composable
private fun Preview_WidgetBlueprint() {
    MaterialTheme {
        WidgetBlueprint(WidgetModel(opacity = 0.5f))
    }
}

private val previewEvents = listOf(
    EventModel(
        1,
        "Bicycling on every Wednesday evening",
        Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); set(Calendar.HOUR_OF_DAY, 11) }.time,
        Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); set(Calendar.HOUR_OF_DAY, 12) }.time,
    ),
    EventModel(
        2,
        "Go waltzing to the zoo",
        Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); set(Calendar.HOUR_OF_DAY, 12) }.time,
        Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); set(Calendar.HOUR_OF_DAY, 14) }.time,
    ),
    EventModel(
        3,
        "Lazing on a Sunday afternoon",
        Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); set(Calendar.HOUR_OF_DAY, 12) }.time,
        Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); set(Calendar.HOUR_OF_DAY, 14) }.time,
    ),
)
