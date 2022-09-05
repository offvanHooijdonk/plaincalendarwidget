package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val PlainThemeShapes =
    Shapes(
        large = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    )

val WidgetItemShape: Shape @Composable get() =  RoundedCornerShape(D.spacingM)
