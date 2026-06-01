@file:Suppress("MemberVisibilityCanBePrivate")

package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface AppDimensions {
    /** 1dp */
    val spacingXXS: Dp get() = 1.dp

    /** 2dp */
    val spacingXS: Dp get() = 2.dp

    /** 4dp */
    val spacingS: Dp get() = 4.dp

    /** 6dp */
    val spacingSM: Dp get() = 6.dp

    /** 8dp */
    val spacingM: Dp get() = 8.dp

    /** 12dp */
    val spacingML: Dp get() = 12.dp

    /** 16dp */
    val spacingL: Dp get() = 16.dp

    /** 24dp */
    val spacingXL: Dp get() = 24.dp

    /** 32dp */
    val spacingXXL: Dp get() = 32.dp

    val widgetPaddingH: Dp get() = spacingM
    val widgetPaddingV: Dp get() = spacingM
    val listSpacingV: Dp get() = spacingS
    val eventItemPaddingV: Dp get() = spacingM
    val eventItemPaddingH: Dp get() = spacingM
    val eventColorSpacing: Dp get() = spacingM
    val eventColorMarkSize: Dp get() = 12.dp
    val eventColorMarkRadiusCircle: Dp get() = spacingSM
    val eventColorMarkRadiusSquare: Dp get() = spacingXS
    val dialogCornerRadius: Dp get() = 28.dp
    val widgetCornerRadius: Dp get() = 28.dp
    val screenPaddingV: Dp get() = spacingL
    val screenPaddingH: Dp get() = spacingL
    val styleBlockHeight: Dp get() = 80.dp
    val eventMarkSpacing: Dp get() = spacingXXL
}

object Dimensions : AppDimensions

object GlanceDimensions : AppDimensions {
    override val spacingXXS: Dp get() = 1.dp
    override val spacingXS: Dp get() = 2.dp
    override val spacingS: Dp get() = 3.dp
    override val spacingSM: Dp get() = 4.dp
    override val spacingM: Dp get() = 6.dp
    override val spacingL: Dp get() = 12.dp
    override val spacingXXL: Dp get() = 24.dp

    override val widgetPaddingV: Dp get() = spacingM + spacingXXS

    override val eventItemPaddingV: Dp get() = spacingS
    override val eventColorMarkRadiusCircle: Dp get() = 5.dp
    override val eventColorMarkSize: Dp get() = 10.dp
}

object TabletDimensions : AppDimensions {

}

@Composable
fun dimens() = LocalDimensions.current

@Composable
fun glanceDimens() = LocalGlanceDimensions.current

val LocalDimensions = compositionLocalOf<AppDimensions> { Dimensions }
val LocalGlanceDimensions = compositionLocalOf<AppDimensions> { GlanceDimensions }
