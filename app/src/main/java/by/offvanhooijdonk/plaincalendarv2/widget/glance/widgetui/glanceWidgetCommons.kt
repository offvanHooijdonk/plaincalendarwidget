package by.offvanhooijdonk.plaincalendarv2.widget.glance.widgetui

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.GD

fun createOpenEventIntent(eventId: Long) =
    Intent(Intent.ACTION_VIEW).apply { data = CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(eventId.toString()).build() }

@Composable
fun EventsDivider() { // todo pass modifier here and set paddings from out
    Box(modifier = GlanceModifier.padding(horizontal = GD.eventItemPaddingH)) {
        Box(
            modifier = GlanceModifier
                .height(GD.spacingXXS)
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        ) {}
    }
}

@Composable
fun EventColorMark(eventColor: Color) {
    Box(
        modifier = GlanceModifier
            .size(GD.eventColorMarkSize)
            .background(eventColor)
            .cornerRadius(GD.spacingSM)
    ) {}
    Spacer(modifier = GlanceModifier.width(GD.eventColorSpacing))
}

@Composable
fun EmptyEventsMessage(textStyle: TextStyle) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = GD.eventItemPaddingH, vertical = GD.eventItemPaddingV)
    ) {
        Text(text = LocalContext.current.getString(R.string.events_empty_message), style = textStyle)
    }
}

val EventModel.colorValue: Color get() = eventColor?.let { Color(it.toLong()) } ?: Color.White
