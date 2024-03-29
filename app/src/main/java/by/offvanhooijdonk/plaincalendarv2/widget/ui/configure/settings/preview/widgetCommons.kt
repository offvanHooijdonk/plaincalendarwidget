@file:OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ext.toColor
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun WidgetEventWrapper(widget: WidgetModel, block: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(widget.backgroundColor.toULong()).copy(alpha = widget.opacity),
        shape = RoundedCornerShape(dimens().widgetCornerRadius),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimens().widgetPaddingH,
                vertical = dimens().widgetPaddingV
            )
        ) {
            block()
        }
    }
}

@Composable
fun EventColorMarkAnimated(isShow: Boolean, eventColor: Color, shape: WidgetModel.EventColorShape) {
    AnimatedContent(targetState = isShow) { showColor ->
        when (showColor) {
            true -> Row {
                /*Icon(
                    modifier = Modifier.size(dimens().eventColorMarkSize),
                    painter = painterResource(R.drawable.ic_circle),
                    tint = eventColor,
                    contentDescription = null,
                )*/
                EventColorMark(eventColor, shape)
            }
            false -> Unit
        }
    }
}

/**
 * @param multiplier - only used for Settings dialog
 */
@Composable
fun EventColorMark(eventColor: Color, shape: WidgetModel.EventColorShape, multiplier: Float = 1.0f) {
    val cornerRadius = when (shape) {
        WidgetModel.EventColorShape.CIRCLE -> dimens().eventColorMarkRadiusCircle * multiplier
        WidgetModel.EventColorShape.SQUARE -> dimens().eventColorMarkRadiusSquare * multiplier
    }
    Box(
        modifier = Modifier
            .size(dimens().eventColorMarkSize * multiplier)
            .background(color = eventColor, shape = RoundedCornerShape(cornerRadius))
    )
    Spacer(modifier = Modifier.width(dimens().eventColorSpacing))
}

@Composable
private fun WidgetHeader(widget: WidgetModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens().spacingM),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val textColor = widget.textColor.toColor()
        val textSizeDate =
            (LocalContext.current.resources.getInteger(R.integer.event_date_default_font_size_sp) + widget.textSizeDelta).sp
        Text(
            modifier = Modifier.padding(horizontal = dimens().spacingXS),
            text = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)),
            color = textColor,
            fontSize = textSizeDate,
        )
        Icon(
            modifier = Modifier.size(dimens().spacingL),
            painter = painterResource(R.drawable.ic_settings), tint = textColor, contentDescription = null
        )
    }
}

val DefaultEventPreviewColor = Color.LightGray
