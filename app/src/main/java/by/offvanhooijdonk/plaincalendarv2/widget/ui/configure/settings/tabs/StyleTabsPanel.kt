@file:OptIn(ExperimentalAnimationApi::class)

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.R

@Composable
fun StylesTabsPanel(widget: WidgetModel, onPreviewSettingsChange: (WidgetModel) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val selectedIndex = remember { mutableStateOf(0) }
        TabRow(selectedTabIndex = selectedIndex.value) {
            SettingTabsList.forEachIndexed { index, tab ->
                LeadingIconTab(
                    selected = index == selectedIndex.value,
                    onClick = { selectedIndex.value = index },
                    text = { },
                    icon = { Icon(painterResource(tab.iconRes), contentDescription = null) }
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            // todo update animSpec to just Fade ?
            AnimatedContent(targetState = selectedIndex.value, transitionSpec = { transitionFade }) { state ->
                when (SettingTabsList[state]) {
                    SettingTab.ColorTab -> {
                        val color = remember(widget) { mutableStateOf(Color(widget.backgroundColor.toULong())) }
                        ColorTab(color.value) {
                            color.value = it
                            onPreviewSettingsChange(widget.copy(backgroundColor = it.value.toLong()))
                        }
                    }
                    SettingTab.OpacityTab -> {
                        val opacity = remember(widget) { mutableStateOf(widget.opacity) }
                        OpacityTab(opacity.value) {
                            opacity.value = it
                            onPreviewSettingsChange(widget.copy(opacity = it))
                        }
                    }
                    SettingTab.TextColorTab -> TextColorTab()
                    SettingTab.TextSizeTab -> TextSizeTab()
                }
            }
        }
    }
}

sealed class SettingTab(
    @DrawableRes val iconRes: Int,
) {
    object ColorTab : SettingTab(R.drawable.ic_color_palette)
    object OpacityTab : SettingTab(R.drawable.ic_opacity)
    object TextColorTab : SettingTab(R.drawable.ic_color_text)
    object TextSizeTab : SettingTab(R.drawable.ic_text_size)
}

val SettingTabsList = listOf(
    SettingTab.ColorTab,
    SettingTab.OpacityTab,
    SettingTab.TextColorTab,
    SettingTab.TextSizeTab,
)

private val transitionFade = fadeIn(animationSpec = tween(220, delayMillis = 90)) with fadeOut(animationSpec = tween(90))
