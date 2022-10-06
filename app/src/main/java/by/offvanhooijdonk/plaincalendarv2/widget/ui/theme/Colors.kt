package by.offvanhooijdonk.plaincalendarv2.widget.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

interface IntroColor {
    val colorPreview: Color @Composable get() = Color.Unspecified
    val colorCalendars: Color @Composable get() = Color.Unspecified
    val colorIntroDays: Color @Composable get() = Color.Unspecified
    val colorIntroLayouts: Color @Composable get() = Color.Unspecified
    val colorIntroSettings: Color @Composable get() = Color.Unspecified
    val colorIntroTabs: Color @Composable get() = Color.Unspecified
    val colorApply: Color @Composable get() = Color.Unspecified
}

object IntroColorsLight : IntroColor {
    override val colorPreview: Color @Composable get() = Color(0xFF119DA4)
    override val colorCalendars: Color @Composable get() = MaterialTheme.colors.primary
    override val colorIntroDays: Color @Composable get() = Color(0xFF0A8754)
    override val colorIntroLayouts: Color @Composable get() = Color(0xFFC879FF)//Color(0xFFBD93D8)
    override val colorIntroSettings: Color @Composable get() = Color(0xFFF4D06F)//Color(0xFFD62839)
    override val colorIntroTabs: Color @Composable get() = Color(0xFFEF2D56)//Color(0xFF9381FF)//Color(0xFFBD93D8)
    override val colorApply: Color @Composable get() = MaterialTheme.colors.secondary
}
//0A8754
//BD93D8

object IntroColorsDark : IntroColor {
    override val colorPreview: Color @Composable get() = Color(0xFF119DA4)
    override val colorCalendars: Color @Composable get() = MaterialTheme.colors.primary
    override val colorIntroDays: Color @Composable get() = Color(0xFF0A8754)
    override val colorIntroLayouts: Color @Composable get() = Color.Unspecified
    override val colorIntroSettings: Color @Composable get() = Color(0xFFD62839)
    override val colorIntroTabs: Color @Composable get() = Color(0xFFBD93D8)//Color(0xFFBD93D8)
    override val colorApply: Color @Composable get() = MaterialTheme.colors.secondary
}

val lightColors = lightColors(
    primary = Color(0xFF3f51b5),
    secondary = Color(0xFF26c6da),
    secondaryVariant = Color(0xFF0095a8),
)

val darkColors = darkColors(
    primary = Color(0xFF757de8),
    secondary = Color(0xFF26c6da),
    secondaryVariant = Color(0xFF0095a8),
)

val LocalIntroColors = compositionLocalOf<IntroColor> { IntroColorsLight }
