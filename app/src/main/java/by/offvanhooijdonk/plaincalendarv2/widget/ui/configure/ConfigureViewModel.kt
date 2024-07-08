@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.AppWidgetId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import by.offvanhooijdonk.plaincalendarv2.widget.data.Prefs
import by.offvanhooijdonk.plaincalendarv2.widget.glance.PlainGlanceWidget
import by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs.readWidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs.writeToPrefs
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfigureViewModel(
    private val ctx: Context,
    private val calendarDataSource: CalendarDataSource,
    private val prefs: Prefs,
) : ViewModel() {
    private var initialWidgetModel: WidgetModel? = null

    private val _widgetModel = MutableStateFlow(DummyWidget)
    val widgetModel = _widgetModel.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _allCalendarsResponse = MutableLiveData<LoadState>(LoadState.Idle)
    val calendarsResponse: LiveData<LoadState> = _allCalendarsResponse // todo replace with uiState

    private val _finishScreen = MutableLiveData<FinishResult?>(null)
    val finishScreen: LiveData<FinishResult?> = _finishScreen // TODO replace with SideEffect

    private val _showSettingsSheet = MutableLiveData(Flag(false))
    val showSettingsSheet: LiveData<Flag> = _showSettingsSheet // TODO replace with SideEffect

    private val _widgetIdsList = MutableLiveData<List<GlanceId>>()
    val widgetIdsList: LiveData<List<GlanceId>> = _widgetIdsList // TODO replace with State

    var widgetId: Int? = null // todo replace with #_widgetModel field usage or uiState

    init {
        _uiState.update { it.copy(isIntroPassed = prefs.isIntroPassed) }
    }

    fun passWidgetId(id: Int?) {
        widgetId = id
        viewModelScope.launch {
            widgetId?.let {
                findExistingWidgetId(it)?.let { glanceId ->
                    readWidgetModel(glanceId)
                    loadCalendars()
                } ?: run {
                    _uiState.update { it.copy(loadState = LoadState.Widget.New) }
                    _widgetModel.value = WidgetModel.createDefault(it.toLong())
                        .let { w -> if (!_uiState.value.isIntroPassed) w.copy(days = 1) else w }
                        .also { w -> initialWidgetModel = w.copy() }
                }
            } ?: run {
                //  todo move to separate class
                GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).also {
                    _widgetIdsList.postValue(it)
                }.lastOrNull()?.let { glanceId ->
                    readWidgetModel(glanceId)
                    loadCalendars()
                } ?: run {
                    _uiState.update { it.copy(loadState = LoadState.Widget.Empty) }
                    _widgetModel.update { WidgetModel.createDefault() }
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.OnCalendarsPicked -> _widgetModel.update { state ->
                state.copy(calendars = action.calendars, calendarIds = action.calendars.map { it.id })
            }
            Action.OnCalendarsRequested -> loadCalendars()
            Action.OnSaveChanges -> updateWidget()
            is Action.OnBackgroundColorPick -> _widgetModel.update { it.copy(backgroundColor = action.colorValue) }
            is Action.OnBackgroundOpacityPick -> _widgetModel.update { it.copy(opacity = action.opacity) }
            Action.OnDateAsTextPick -> _widgetModel.update { it.copy(showDateAsTextLabel = !it.showDateAsTextLabel) }
            is Action.OnDaysPick -> _widgetModel.update { it.copy(days = action.days) }
            is Action.OnEventColorShapePick -> _widgetModel.update { it.copy(eventColorShape = action.eventColorShape) }
            is Action.OnLayoutPick -> _widgetModel.update { it.copy(layoutType = action.layoutType) }
            Action.OnShowDividersPick -> _widgetModel.update { it.copy(showEventDividers = !it.showEventDividers) }
            is Action.OnShowEndDatePick -> _widgetModel.update { it.copy(showEndDate = action.showEndDate) }
            Action.OnShowEventColorPick -> _widgetModel.update { it.copy(showEventColor = !it.showEventColor) }
            Action.OnTextBoldPick -> _widgetModel.update { it.copy(textStyleBold = !it.textStyleBold) }
            is Action.OnTextColorPick -> _widgetModel.update { it.copy(textColor = action.colorValue) }
            is Action.OnTextSizeDeltaPick -> _widgetModel.update { it.copy(textSizeDelta = action.textSizeDelta) }
            Action.OnSettingsClick -> onSettingsClick()
            Action.OnPackPressed -> onBackPressed()
            Action.OnExitCanceled -> _uiState.update { it.copy(isShowExitConfirmation = false) }
            Action.OnExitConfirmed -> onExitConfirmed()
            Action.OnIntroPassed -> onIntroPassed()
            Action.OnIntroductionRequested -> onIntroductionRequested()
            is Action.OnWidgetPick -> onWidgetPicked(action.glanceId)
        }
    }

    private fun onWidgetPicked(glanceId: GlanceId) {
        viewModelScope.launch {
            readWidgetModel(glanceId)
            loadCalendars()
        }
    }

    private fun updateWidget() {
        viewModelScope.launch { // todo add ability to call update from other places?
            var updateGlanceId: GlanceId? = null
            GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).forEach { glanceId ->
                if (glanceId.toIntId() == widgetId) {
                    updateGlanceId = glanceId
                    updateAppWidgetState(ctx, glanceId) { prefs ->
                        _widgetModel.value.writeToPrefs(prefs)
                    }
                }
            }
            updateGlanceId?.let { PlainGlanceWidget().update(ctx, it) }
        }

        _finishScreen.value = FinishResult.OK
    }

    private fun loadCalendars() {
        _allCalendarsResponse.value = LoadState.Progress
        viewModelScope.launch {
            val calendars = calendarDataSource.loadCalendars()
// todo fill calendars data separately
            val currentCalendarsSelection = calendars.filter { it.id in _widgetModel.value.calendarIds }
            _widgetModel.update { it.copy(calendars = currentCalendarsSelection) }
            _allCalendarsResponse.postValue(LoadState.Calendars.Success(calendars))
        }
    }

    private fun onSettingsClick() {
        _showSettingsSheet.postValue(Flag(true))
    }

    private fun onBackPressed() {
        if (_uiState.value.loadState in listOf(LoadState.Widget.Success, LoadState.Widget.New)
            && initialWidgetModel?.isEqualSettings(_widgetModel.value) == false
        ) {
            _uiState.update { it.copy(isShowExitConfirmation = true) }
        } else {
            _finishScreen.value = FinishResult.CANCELED
        }
    }

    private fun onExitConfirmed() {
        _uiState.update { it.copy(isShowExitConfirmation = false) }
        _finishScreen.value = FinishResult.CANCELED
    }

    private fun onIntroPassed() {
        prefs.isIntroPassed = true
        _uiState.update { it.copy(isIntroPassed = true) }
        if (_uiState.value.loadState == LoadState.Widget.New) {
            _widgetModel.update { it.copy(days = WidgetModel.DAYS_DEFAULT) }
        }
    }

    private fun onIntroductionRequested() {
        _uiState.update { it.copy(isIntroPassed = false) }
    }

    private suspend fun findExistingWidgetId(widgetId: Int) =
        GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).find { it.toIntId() == widgetId }

    private suspend fun readWidgetModel(glanceId: GlanceId) {
        widgetId = glanceId.toIntId()
        val state = getAppWidgetState(ctx, PreferencesGlanceStateDefinition, glanceId)
        val widget = state.readWidgetModel(widgetId?.toLong())

        _widgetModel.value = widget.also { w -> initialWidgetModel = w.copy() }
        _uiState.update { it.copy(loadState = LoadState.Widget.Success) }
    }

    sealed interface Action {
        data object OnCalendarsRequested : Action
        data object OnSaveChanges : Action
        data object OnSettingsClick : Action
        data object OnDateAsTextPick : Action
        data object OnShowEventColorPick : Action
        data object OnShowDividersPick : Action
        data object OnPackPressed : Action
        data class OnCalendarsPicked(val calendars: List<CalendarModel>) : Action
        data class OnLayoutPick(val layoutType: WidgetModel.LayoutType) : Action
        data class OnBackgroundColorPick(val colorValue: Long) : Action
        data class OnBackgroundOpacityPick(val opacity: Float) : Action
        data class OnTextColorPick(val colorValue: Long) : Action
        data class OnTextSizeDeltaPick(val textSizeDelta: Int) : Action
        data object OnTextBoldPick : Action
        data object OnIntroductionRequested : Action
        data object OnIntroPassed : Action
        data object OnExitCanceled : Action
        data object OnExitConfirmed : Action
        data class OnDaysPick(val days: Int) : Action
        data class OnShowEndDatePick(val showEndDate: WidgetModel.ShowEndDate) : Action
        data class OnEventColorShapePick(val eventColorShape: WidgetModel.EventColorShape) : Action
        data class OnWidgetPick(val glanceId: GlanceId) : Action
    }

    data class UiState(
        val isShowExitConfirmation: Boolean = false,
        val isIntroPassed: Boolean = true,
        val loadState: LoadState = LoadState.Idle,
    )

    enum class FinishResult {
        OK, CANCELED
    }

}

fun GlanceId.toIntId() = (this as AppWidgetId).appWidgetId


sealed interface LoadState {
    data object Idle : LoadState
    data object Progress : LoadState
    class Error(val msg: String?) : LoadState

    sealed interface Widget : LoadState {
        data object Empty : LoadState
        data object Success : LoadState
        data object New : LoadState
    }

    sealed interface Calendars : LoadState {
        class Success(val list: List<CalendarModel>) : LoadState // todo convert to object as Widget
    }
}

class Flag(val value: Boolean)
