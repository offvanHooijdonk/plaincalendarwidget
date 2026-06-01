package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

interface AppColors {
    val colorPreview: Color @Composable get() = Color.Unspecified
    val colorCalendars: Color @Composable get() = Color.Unspecified
    val colorIntroDays: Color @Composable get() = Color.Unspecified
    val colorIntroLayouts: Color @Composable get() = Color.Unspecified
    val colorIntroSettings: Color @Composable get() = Color.Unspecified
    val colorIntroTabs: Color @Composable get() = Color.Unspecified
    val colorApply: Color @Composable get() = Color.Unspecified
}

object AppColorsLight : AppColors {
    override val colorPreview: Color @Composable get() = Color(0xFF119DA4)
    override val colorCalendars: Color @Composable get() = MaterialTheme.colorScheme.primary
    override val colorIntroDays: Color @Composable get() = Color(0xFF0A8754)
    override val colorIntroLayouts: Color @Composable get() = Color(0xFFC879FF)
    override val colorIntroSettings: Color @Composable get() = Color(0xFFF4D06F)
    override val colorIntroTabs: Color @Composable get() = Color(0xFFEF2D56)
    override val colorApply: Color @Composable get() = MaterialTheme.colorScheme.secondary
}

object AppColorsDark : AppColors {
    override val colorPreview: Color @Composable get() = Color(0xFF119DA4)
    override val colorCalendars: Color @Composable get() = MaterialTheme.colorScheme.primary
    override val colorIntroDays: Color @Composable get() = Color(0xFF0A8754)
    override val colorIntroLayouts: Color @Composable get() = Color.Unspecified
    override val colorIntroSettings: Color @Composable get() = Color(0xFFD62839)
    override val colorIntroTabs: Color @Composable get() = Color(0xFFBD93D8)//Color(0xFFBD93D8)
    override val colorApply: Color @Composable get() = MaterialTheme.colorScheme.secondary
}

val lightColors = lightColorScheme(
    primary = Color(0xFF3f51b5),
    secondary = Color(0xFF26c6da),
    secondaryContainer = Color(0xFF0095a8),
    surface = Color(0xFFFAFAFA),
    onSurface = Color(0xFF303651),
)

val darkColors = darkColorScheme(
    primary = Color(0xFF757de8),
    secondary = Color(0xFF26c6da),
    secondaryContainer = Color(0xFF0095a8),
    surface = Color(0xFF303651),
    onSurface = Color(0xFFFAFAFA),
)

val LocalIntroColors = compositionLocalOf<AppColors> { AppColorsLight }
