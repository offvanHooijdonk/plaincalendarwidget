@file:Suppress("MemberVisibilityCanBePrivate")

package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.unit.Dimension
import by.offvanhooijdonk.plaincalendarv2.widget.R

interface WidgetDimensions {
    val spacingS: Dp @Composable @ReadOnlyComposable get() = 4.dp
    val spacingM: Dp @Composable @ReadOnlyComposable get() = 8.dp
    val spacingL: Dp @Composable @ReadOnlyComposable get() = 16.dp

    val widgetPaddingH: Dp @Composable @ReadOnlyComposable get() = Dimens.spacingM
    val widgetPaddingV: Dp @Composable @ReadOnlyComposable get() = Dimens.spacingM
    val listSpacingV: Dp @Composable @ReadOnlyComposable get() = Dimens.spacingS
    val eventItemPaddingV: Dp @Composable @ReadOnlyComposable get() = Dimens.spacingM
    val eventItemPaddingH: Dp @Composable @ReadOnlyComposable get() = Dimens.spacingM
    val eventColorSpacing: Dp @Composable @ReadOnlyComposable get() = Dimens.spacingM

    val eventColorMarkSize: Dp @Composable @ReadOnlyComposable get() = 12.dp
}

object Dimens: WidgetDimensions {

    val screenPaddingV: Dp @Composable @ReadOnlyComposable get() = spacingL
    val screenPaddingH: Dp @Composable @ReadOnlyComposable get() = spacingL
    val widgetCornerRadius: Dp @Composable @ReadOnlyComposable get() = 28.dp
}

object GlanceDimens : WidgetDimensions {
    override val spacingS: Dp @Composable @ReadOnlyComposable get() = 3.dp
    override val spacingM: Dp @Composable @ReadOnlyComposable get() = 6.dp
    override val spacingL: Dp @Composable @ReadOnlyComposable get() = 12.dp

    override val eventColorMarkSize: Dp @Composable @ReadOnlyComposable get() = 8.dp
}


typealias D = Dimens
typealias GD = GlanceDimens
