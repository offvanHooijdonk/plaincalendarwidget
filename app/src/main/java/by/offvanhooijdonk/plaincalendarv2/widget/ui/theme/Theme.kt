package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_XLARGE
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.*

@Composable
fun PlainTheme(isDarkMode: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val isTablet = LocalConfiguration.current.screenLayout in listOf(SCREENLAYOUT_SIZE_LARGE, SCREENLAYOUT_SIZE_XLARGE)

    CompositionLocalProvider(
        LocalIntroColors provides (if (isDarkMode) AppColorsDark else AppColorsLight),
        LocalDimensions provides (if (isTablet) TabletDimensions else Dimensions)
    ) {
        MaterialTheme(
            colorScheme = if (isDarkMode) darkColors else lightColors,
            typography = Typography,
            shapes = PlainThemeShapes,
        ) {
            //AppSystemBarsColors()
            content()
        }
    }
}

@Composable
fun AppSystemBarsColors(darkTheme: Boolean = isSystemInDarkTheme()) {
    /*val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primarySurface
    val navBarColor = MaterialTheme.colorScheme.background*/
    //val useDarkIcons = true//!darkTheme

    /*SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
        )

        systemUiController.setNavigationBarColor(
            color = navBarColor,
        )
    }*/
}
