package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.annotation.DrawableRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import by.offvanhooijdonk.plaincalendarv2.widget.R
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.ConfigureViewModel.Action
import by.offvanhooijdonk.plaincalendarv2.widget.ui.theme.dimens
import by.offvanhooijdonk.plaincalendarv2.widget.ui.util.isTextDeltaValid

@Composable
fun StylesTabsPanel(widget: WidgetModel, onAction: (Action) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val selectedIndex = remember { mutableIntStateOf(0) }
        TabRow(selectedTabIndex = selectedIndex.intValue) {
            SettingTabsList.forEachIndexed { index, tab ->
                LeadingIconTab(
                    selected = index == selectedIndex.intValue,
                    onClick = { selectedIndex.intValue = index },
                    text = { },
                    icon = { Icon(painterResource(tab.iconRes), contentDescription = null) }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens().styleBlockHeight)
        ) {
            AnimatedContent(targetState = selectedIndex.intValue, transitionSpec = { transitionFade }) { state ->
                when (SettingTabsList[state]) {
                    SettingTab.ColorTab -> {
                        val backColor = remember(widget) { mutableStateOf(Color(widget.backgroundColor.toULong())) }
                        BackgroundColorTab(backColor.value) {
                            backColor.value = it
                            onAction(Action.OnBackgroundColorPick(it.value.toLong()))
                        }
                    }
                    SettingTab.OpacityTab -> {
                        val opacity = remember(widget) { mutableFloatStateOf(widget.opacity) }
                        OpacityTab(opacity.floatValue) {
                            opacity.floatValue = it
                            onAction(Action.OnBackgroundOpacityPick(it))
                        }
                    }
                    SettingTab.TextColorTab -> {
                        val textColor = remember(widget) { mutableStateOf(Color(widget.textColor.toULong())) }
                        TextColorTab(textColor.value) {
                            textColor.value = it
                            onAction(Action.OnTextColorPick(it.value.toLong()))
                        }
                    }
                    SettingTab.TextSizeTab -> {
                        val ctx = LocalContext.current
                        val textSizeDelta = remember(widget) { widget.textSizeDelta }
                        val textStyleBold = remember(widget) { widget.textStyleBold }
                        TextSizeTab(
                            textSizeDelta,
                            textStyleBold,
                            onSizeChange = { newValue ->
                                if (isTextDeltaValid(ctx, newValue)) {
                                    onAction(Action.OnTextSizeDeltaPick(newValue))
                                }
                            },
                            onStyleChange = { onAction(Action.OnTextBoldPick) }
                        )
                    }
                }
            }
        }
    }
}

sealed class SettingTab(
    @DrawableRes val iconRes: Int,
) {
    data object ColorTab : SettingTab(R.drawable.ic_color_palette)
    data object OpacityTab : SettingTab(R.drawable.ic_opacity)
    data object TextColorTab : SettingTab(R.drawable.ic_color_text)
    data object TextSizeTab : SettingTab(R.drawable.ic_text_size)
}

val SettingTabsList = listOf(
    SettingTab.ColorTab,
    SettingTab.OpacityTab,
    SettingTab.TextColorTab,
    SettingTab.TextSizeTab,
)

private val transitionFade = fadeIn(animationSpec = tween(220, delayMillis = 90)) togetherWith fadeOut(animationSpec = tween(90))
