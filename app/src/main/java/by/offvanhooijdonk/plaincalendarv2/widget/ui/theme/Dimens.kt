@file:Suppress("MemberVisibilityCanBePrivate")

package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface WidgetDimensions {
    /** 1dp */
    val spacingXXS: Dp /*@Composable @ReadOnlyComposable*/ get() = 1.dp

    /** 2dp */
    val spacingXS: Dp /*@Composable @ReadOnlyComposable*/ get() = 2.dp

    /** 4dp */
    val spacingS: Dp /*@Composable @ReadOnlyComposable*/ get() = 4.dp

    /** 6dp */
    val spacingSM: Dp /*@Composable @ReadOnlyComposable*/ get() = 6.dp

    /** 8dp */
    val spacingM: Dp /*@Composable @ReadOnlyComposable*/ get() = 8.dp

    /** 12dp */
    val spacingML: Dp /*@Composable @ReadOnlyComposable*/ get() = 12.dp

    /** 16dp */
    val spacingL: Dp /*@Composable @ReadOnlyComposable*/ get() = 16.dp

    /** 24dp */
    val spacingXL: Dp /*@Composable @ReadOnlyComposable*/ get() = 24.dp

    /** 32dp */
    val spacingXXL: Dp /*@Composable @ReadOnlyComposable*/ get() = 32.dp

    val widgetPaddingH: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingM
    val widgetPaddingV: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingM
    val listSpacingV: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingS
    val eventItemPaddingV: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingM
    val eventItemPaddingH: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingM
    val eventColorSpacing: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingM
    val eventColorMarkSize: Dp /*@Composable @ReadOnlyComposable*/ get() = 12.dp
}

object Dimens : WidgetDimensions {
    val screenPaddingV: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingL
    val screenPaddingH: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingL
    val widgetCornerRadius: Dp /*@Composable @ReadOnlyComposable*/ get() = 28.dp
    val dialogCornerRadius: Dp /*@Composable @ReadOnlyComposable*/ get() = 28.dp

    val styleBlockHeight: Dp = 80.dp
}

object GlanceDimens : WidgetDimensions {
    override val spacingXXS: Dp /*@Composable @ReadOnlyComposable*/ get() = 1.dp
    override val spacingXS: Dp /*@Composable @ReadOnlyComposable*/ get() = 2.dp
    override val spacingS: Dp /*@Composable @ReadOnlyComposable*/ get() = 3.dp
    override val spacingSM: Dp /*@Composable @ReadOnlyComposable*/ get() = 4.dp
    override val spacingM: Dp /*@Composable @ReadOnlyComposable*/ get() = 6.dp
    override val spacingL: Dp /*@Composable @ReadOnlyComposable*/ get() = 12.dp
    override val spacingXXL: Dp /*@Composable @ReadOnlyComposable*/ get() = 24.dp

    override val widgetPaddingV: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingM + GD.spacingXXS

    override val eventItemPaddingV: Dp /*@Composable @ReadOnlyComposable*/ get() = spacingS
    override val eventColorMarkSize: Dp /*@Composable @ReadOnlyComposable*/ get() = 8.dp
}

typealias D = Dimens
typealias GD = GlanceDimens
