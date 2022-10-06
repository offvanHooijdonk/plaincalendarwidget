package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PlainTheme(isDarkMode: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalIntroColors provides (if (isDarkMode) IntroColorsDark else IntroColorsLight)) {

        MaterialTheme(
            colors = if (isDarkMode) darkColors else lightColors,
            shapes = PlainThemeShapes,
        ) {
            AppSystemBarsColors()
            content()
        }
    }
}

@Composable
fun AppSystemBarsColors(darkTheme: Boolean = isSystemInDarkTheme()) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colors.primarySurface
    val navBarColor = MaterialTheme.colors.background
    //val useDarkIcons = true//!darkTheme

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
        )

        systemUiController.setNavigationBarColor(
            color = navBarColor,
        )
    }
}
