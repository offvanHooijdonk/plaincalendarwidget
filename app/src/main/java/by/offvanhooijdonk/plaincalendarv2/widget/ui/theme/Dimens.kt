package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import by.offvanhooijdonk.plaincalendarv2.widget.R

object Dimens {
    val spacingXS: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.spacing_xs)
    val spacingS: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.spacing_s)
    val spacingM: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.spacing_m)
    val spacingL: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.spacing_l)

    val screenPaddingV: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.screen_padding_v)
    val screenPaddingH: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.screen_padding_h)
    val widgetCornerRadius: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.widget_corner_radius)
    val widgetPaddingH: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.widget_padding_h)
    val widgetPaddingV: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.widget_padding_v)

    val eventColorMarlSize: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.event_color_mark_size)
}

object DimensGlide {
    val spacingS: Dp @Composable @ReadOnlyComposable get() = dimensionResource(R.dimen.spacing_s)
}

typealias D = Dimens
