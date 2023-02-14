package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable

val PlainThemeShapes @Composable get() =
    Shapes(
        medium = RoundedCornerShape(dimens().dialogCornerRadius),
        large = RoundedCornerShape(topStart = dimens().dialogCornerRadius, topEnd = dimens().dialogCornerRadius),
    )

val WidgetItemShape @Composable get() =  RoundedCornerShape(dimens().spacingM)
val AlertDialogShape @Composable get() = RoundedCornerShape(dimens().dialogCornerRadius)
