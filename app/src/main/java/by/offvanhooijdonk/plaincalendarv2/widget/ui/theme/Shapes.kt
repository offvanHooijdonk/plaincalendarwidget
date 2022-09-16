package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

val PlainThemeShapes =
    Shapes(
        medium = RoundedCornerShape(D.dialogCornerRadius),
        large = RoundedCornerShape(topStart = D.dialogCornerRadius, topEnd = D.dialogCornerRadius),
    )

val WidgetItemShape: Shape @Composable get() =  RoundedCornerShape(D.spacingM)
