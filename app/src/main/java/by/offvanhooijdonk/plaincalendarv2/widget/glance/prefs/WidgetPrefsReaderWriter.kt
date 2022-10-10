package by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs

import androidx.datastore.preferences.core.*
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel

private const val KEY_DAYS = "key_days"
private const val KEY_BACK_COLOR = "key_background_color"
private const val KEY_BACK_OPACITY = "key_back_opacity"
private const val KEY_TEXT_COLOR = "key_text_color"
private const val KEY_TEXT_SIZE = "key_text_size"
private const val KEY_TEXT_BOLD = "key_text_bold"
private const val KEY_CALENDARS_IDS = "key_calendars_ids"
private const val KEY_SHOW_EVENTS_COLORS = "key_show_event_colors"
private const val KEY_SHOW_EVENTS_DIVIDERS = "key_show_events_dividers"
private const val KEY_SHOW_DATE_AS_TEXT_LABEL = "key_show_date_as_text_label"
private const val KEY_SHOW_END_DATE = "key_show_end_date"
private const val KEY_LAYOUT_TYPE = "key_show_end_date"

private val keyDays = intPreferencesKey(KEY_DAYS)
private val keyBackgroundColor = longPreferencesKey(KEY_BACK_COLOR)
private val keyBackgroundOpacity = floatPreferencesKey(KEY_BACK_OPACITY)
private val keyTextColor = longPreferencesKey(KEY_TEXT_COLOR)
private val keyTextSize = intPreferencesKey(KEY_TEXT_SIZE)
private val keyTextBold = booleanPreferencesKey(KEY_TEXT_BOLD)
private val keyCalendarsIds = stringSetPreferencesKey(KEY_CALENDARS_IDS)
private val keyShowEventColor = booleanPreferencesKey(KEY_SHOW_EVENTS_COLORS)
private val keyShowEventDividers = booleanPreferencesKey(KEY_SHOW_EVENTS_DIVIDERS)
private val keyShowDateAsTextLabel = booleanPreferencesKey(KEY_SHOW_DATE_AS_TEXT_LABEL)
private val keyShowEndDate = stringPreferencesKey(KEY_SHOW_END_DATE)
private val keyLayoutType = stringPreferencesKey(KEY_LAYOUT_TYPE)

fun WidgetModel.writeToPrefs(prefs: MutablePreferences) {
    prefs[keyDays] = days
    prefs[keyBackgroundColor] = backgroundColor
    prefs[keyBackgroundOpacity] = opacity
    prefs[keyTextColor] = textColor
    prefs[keyTextBold] = textStyleBold
    prefs[keyTextSize] = textSizeDelta
    prefs[keyCalendarsIds] = calendarIds.map { it.toString() }.toSet()
    prefs[keyShowEventColor] = showEventColor
    prefs[keyShowEventDividers] = showEventDividers
    prefs[keyShowDateAsTextLabel] = showDateAsTextLabel
    prefs[keyShowEndDate] = showEndDate.name
    prefs[keyLayoutType] = layoutType.name
}

fun Preferences.readWidgetModel(glanceId: Long? = null): WidgetModel =
    with(WidgetModel.createDefault()) {
        copy(
            id = glanceId ?: id,
            days = get(keyDays) ?: days,
            backgroundColor = get(keyBackgroundColor) ?: backgroundColor,
            opacity = get(keyBackgroundOpacity) ?: opacity,
            textColor = get(keyTextColor) ?: textColor,
            textSizeDelta = get(keyTextSize) ?: textSizeDelta,
            textStyleBold = get(keyTextBold) ?: textStyleBold,
            calendarIds = get(keyCalendarsIds)?.map { it.toLong() }?.toList() ?: calendarIds,
            showEventColor = get(keyShowEventColor) ?: showEventColor,
            showEventDividers = get(keyShowEventDividers) ?: showEventDividers,
            showDateAsTextLabel = get(keyShowDateAsTextLabel) ?: showDateAsTextLabel,
            showEndDate = get(keyShowEndDate)?.let { WidgetModel.ShowEndDate.valueOfOrDefault(it) } ?: showEndDate,
            layoutType = get(keyLayoutType)?.let { WidgetModel.LayoutType.valueOfOrDefault(it) } ?: layoutType,
        )
    }
