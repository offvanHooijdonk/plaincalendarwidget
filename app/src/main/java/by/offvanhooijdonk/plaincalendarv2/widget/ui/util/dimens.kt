package by.offvanhooijdonk.plaincalendarv2.widget.ui.util

import android.content.Context
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel

fun getTitleTextSize(context: Context, widgetModel: WidgetModel) =
    (context.resources.getInteger(R.integer.event_text_default_font_size_sp) + widgetModel.textSizeDelta).sp

fun getDateTextSize(context: Context, widgetModel: WidgetModel) =
    (context.resources.getInteger(R.integer.event_date_default_font_size_sp) + widgetModel.textSizeDelta).sp

fun isTextDeltaValid(context: Context, textDelta: Int): Boolean =
    context.resources.getInteger(R.integer.event_text_default_font_size_sp) + textDelta in
            context.resources.getInteger(R.integer.event_text_min_font_size_sp)..context.resources.getInteger(R.integer.event_text_max_font_size_sp)
