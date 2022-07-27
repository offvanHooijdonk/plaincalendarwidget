package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure.settings.tabs

import androidx.annotation.DrawableRes
import by.offvanhooijdonk.plaincalendar.widget.R

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
