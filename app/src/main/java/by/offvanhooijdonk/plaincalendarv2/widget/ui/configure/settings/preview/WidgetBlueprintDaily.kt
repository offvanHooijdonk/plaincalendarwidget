package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.D
import java.time.LocalDateTime

@Composable
fun WidgetBlueprintDaily(widget: WidgetModel) {
    WidgetEventWrapper(widget) {
        val events = previewEvents
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = D.listSpacingV)
        ) {
            itemsIndexed(events, key = { _, item -> item.id }) { index, item ->
                item.title

                Divider()
            }
        }
    }
}

@Composable
private fun EventDayLabelItem(widget: WidgetModel, eventModel: EventModel) {

}

@Composable
private fun EventItem(widget: WidgetModel, eventModel: EventModel) {

}

@Preview
@Composable
private fun Preview_WidgetBlueprintDaily() {
    MaterialTheme {
        WidgetBlueprintDaily(DummyWidget.copy(opacity = 0.5f))
    }
}

private val Today = LocalDateTime.now()

private val previewEvents: List<EventModel>
    @Composable
    get() = listOf(
        EventModel(
            id = 1,
            title = stringResource(R.string.sample_event_sunday),
            dateStart = Today,
            dateEnd = Today.plusHours(1),
        ),
        EventModel(
            id = 1,
            title = stringResource(R.string.sample_event_monday),
            dateStart = Today,
            dateEnd = Today.plusHours(1),
        ),
        EventModel(
            id = 1,
            title = stringResource(R.string.sample_event_tuesday),
            dateStart = Today,
            dateEnd = Today.plusHours(1),
        ),
        EventModel(
            id = 1,
            title = stringResource(R.string.sample_event_wednesday),
            dateStart = Today.plusDays(4),
            dateEnd = Today.plusDays(5),
            isAllDay = true,
        ),

    )
