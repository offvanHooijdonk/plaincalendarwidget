package by.offvanhooijdonk.plaincalendarv2.widget.ui.intro

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.LocalIntroColors
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import com.canopas.lib.showcase.ShowcaseStyle

private const val IntroAlpha = 1.0f

val IntroStylePreview
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorPreview,
        backgroundAlpha = IntroAlpha,
    )

@Composable
fun IntroPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens().spacingL)
    ) {
        Text(stringResource(R.string.intro_widget_preview), style = IntroHeaderTextStyle)
    }
}

val IntroStyleConfigureCalendars
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorCalendars,
        backgroundAlpha = 1.0f,
    )

@Composable
fun IntroConfigureCalendars() {
    Column {
        Text(stringResource(R.string.intro_configure_calendars), style = IntroHeaderTextStyle)
    }
}

val IntroStyleDays
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorIntroDays,
        backgroundAlpha = 1.0f,
    )

@Composable
fun IntroDays() {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(dimens().spacingXXL),
                painter = painterResource(R.drawable.ic_swipe),
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(dimens().spacingL))
            Text(stringResource(R.string.intro_days), style = IntroHeaderTextStyle)
        }
    }
}

val IntroStyleLayouts
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorIntroLayouts,
        backgroundAlpha = 1.0f,
    )

@Composable
fun IntroLayouts() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens().spacingL)
    ) {
        Text(stringResource(R.string.intro_layouts), style = IntroHeaderTextStyle)
    }
}

val IntroStyleSettings
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorIntroSettings,
        backgroundAlpha = 1.0f,
    )

@Composable
fun IntroSettings() {
    Box(modifier = Modifier.width(280.dp)) {
        Text(stringResource(R.string.intro_settings), style = IntroHeaderTextStyle)
    }
}

val IntroStyleColorsTabs
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorIntroTabs,
        backgroundAlpha = 1.0f,
    )

@Composable
fun IntroColorsTabs() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens().spacingL)
    ) {
        Text(stringResource(R.string.intro_colors_tabs), style = IntroHeaderTextStyle)
    }
}

val IntroStyleApplyButton
    @Composable get() = ShowcaseStyle.Default.copy(
        backgroundColor = LocalIntroColors.current.colorApply,
        backgroundAlpha = IntroAlpha,
    )

@Composable
fun IntroApplyButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens().spacingL)
    ) {
        Text(stringResource(R.string.intro_apply_button), style = IntroHeaderTextStyle)
    }
}

val IntroHeaderTextStyle = TextStyle(color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

enum class IntroTargets {
    PREVIEW, CONFIGURE_CALENDARS, DAYS_NUMBER, LAYOUTS, SETTINGS, COLOR_TABS, APPLY
}
