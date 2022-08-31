package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PlainTheme(isDarkMode: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (isDarkMode) darkColors else lightColors,
        shapes = PlainThemeShapes,
    ) {
        content()
    }
}
