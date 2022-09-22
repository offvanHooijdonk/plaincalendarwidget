package by.offvanhooijdonk.plaincalendarv2.widget.glance.widgetui

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.GD

fun createOpenEventIntent(eventId: Long) =
    Intent(Intent.ACTION_VIEW).apply { data = CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(eventId.toString()).build() }

@Composable
fun EventsDivider() {
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
            .cornerRadius(GD.spacingS) // todo use dimens or image
    ) {}
    Spacer(modifier = GlanceModifier.width(GD.eventColorSpacing))
}

val EventModel.colorValue: Color get() = eventColor?.let { Color(it.toLong()) } ?: Color.White
