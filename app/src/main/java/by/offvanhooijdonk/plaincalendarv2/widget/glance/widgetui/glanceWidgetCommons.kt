package by.offvanhooijdonk.plaincalendarv2.widget.glance.widgetui

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.EventModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.glanceDimens

fun createOpenEventIntent(eventId: Long) =
    Intent(Intent.ACTION_VIEW).apply { data = CalendarContract.Events.CONTENT_URI.buildUpon().appendPath(eventId.toString()).build() }

@Composable
fun EventsDivider() { // todo pass modifier here and set paddings from out
    Box(modifier = GlanceModifier.padding(horizontal = glanceDimens().eventItemPaddingH)) {
        Box(
            modifier = GlanceModifier
                .height(glanceDimens().spacingXXS)
                .fillMaxWidth()
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
        ) {}
    }
}

@Composable
fun EventColorMark(eventColor: Color, shape: WidgetModel.EventColorShape) {
    val cornerRadius = when(shape) {
        WidgetModel.EventColorShape.CIRCLE -> glanceDimens().eventColorMarkRadiusCircle
        WidgetModel.EventColorShape.SQUARE -> glanceDimens().eventColorMarkRadiusSquare
    }
    Box(
        modifier = GlanceModifier
            .size(glanceDimens().eventColorMarkSize)
            .background(eventColor)
            .cornerRadius(cornerRadius)
    ) {}
    Spacer(modifier = GlanceModifier.width(glanceDimens().eventColorSpacing))
}

@Composable
fun EmptyEventsMessage(textStyle: TextStyle) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(horizontal = glanceDimens().eventItemPaddingH, vertical = glanceDimens().eventItemPaddingV)
    ) {
        Text(text = LocalContext.current.getString(R.string.events_empty_message), style = textStyle)
    }
}

val EventModel.colorValue: Color get() = eventColor?.let { Color(it.toLong()) } ?: Color.White
