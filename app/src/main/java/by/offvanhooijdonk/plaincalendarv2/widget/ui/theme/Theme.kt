package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalConfiguration
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PlainTheme(isDarkMode: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val isTablet = LocalConfiguration.current.screenLayout in listOf(SCREENLAYOUT_SIZE_LARGE, SCREENLAYOUT_SIZE_XLARGE)

    CompositionLocalProvider(
        LocalIntroColors provides (if (isDarkMode) AppColorsDark else AppColorsLight),
        LocalDimensions provides (if (isTablet) TabletDimensions else Dimensions)
    ) {
        MaterialTheme(
            colors = if (isDarkMode) darkColors else lightColors,
            typography = Typography,
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
