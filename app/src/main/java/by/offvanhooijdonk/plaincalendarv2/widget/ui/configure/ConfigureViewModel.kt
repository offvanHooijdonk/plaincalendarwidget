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
import kotlinx.coroutines.launch

class ConfigureViewModel(
    private val ctx: Context,
    private val calendarDataSource: CalendarDataSource,
    private val prefs: Prefs,
) : ViewModel() {
    private var initialWidgetModel: WidgetModel? = null

    private val _loadResult = MutableLiveData<Result>(Result.Idle)
    val loadResult: LiveData<Result> = _loadResult

    private val _widgetModel = MutableLiveData(DummyWidget)
    val widgetModel: LiveData<WidgetModel> = _widgetModel

    private val _allCalendarsResponse = MutableLiveData<Result>(Result.Idle)
    val calendarsResponse: LiveData<Result> = _allCalendarsResponse

    private val _finishScreen = MutableLiveData<FinishResult?>(null)
    val finishScreen: LiveData<FinishResult?> = _finishScreen

    private val _showSettingsSheet = MutableLiveData(Flag(false))
    val showSettingsSheet: LiveData<Flag> = _showSettingsSheet

    private val _showExitConfirmation = MutableLiveData(false)
    val showExitConfirmation: LiveData<Boolean> = _showExitConfirmation

    private val _isIntroPassed = MutableLiveData(true)
    val isIntroPassed: LiveData<Boolean> = _isIntroPassed

    private val _widgetIdsList = MutableLiveData<List<GlanceId>>()
    val widgetIdsList: LiveData<List<GlanceId>> = _widgetIdsList

    var widgetId: Int? = null // todo replace with #_widgetModel field usage ?

    init {
        _isIntroPassed.value = prefs.isIntroPassed
    }

    fun passWidgetId(id: Int?) {
        widgetId = id
        widgetId?.let {
            _loadResult.value = Result.Widget.New
            _widgetModel.value = WidgetModel.createDefault(it.toLong())
                .let { w -> if (_isIntroPassed.value == false) w.copy(days = 1) else w }
                .also { w -> initialWidgetModel = w.copy() }
        } ?: run {
            viewModelScope.launch {
                //  todo move to separate class
                GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).also {
                    _widgetIdsList.postValue(it)
                }.lastOrNull()?.let { glanceId ->
                    readWidgetModel(glanceId)
                    loadCalendars()
                } ?: run {
                    _loadResult.value = Result.Widget.Empty
                    _widgetModel.value = WidgetModel.createDefault()
                }
            }
        }
    }

    fun onWidgetPicked(glanceId: GlanceId) {
        viewModelScope.launch {
            readWidgetModel(glanceId)
            loadCalendars()
        }
    }

    fun updateWidget() {
        viewModelScope.launch { // todo add ability to call update from other places?
            var updateGlanceId: GlanceId? = null
            GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).forEach { glanceId ->
                if (glanceId.toIntId() == widgetId) {
                    updateGlanceId = glanceId
                    updateAppWidgetState(ctx, glanceId) { prefs ->
                        _widgetModel.value?.writeToPrefs(prefs)
                    }
                }
            }
            updateGlanceId?.let { PlainGlanceWidget().update(ctx, it) }
        }

        _finishScreen.value = FinishResult.OK
    }

    fun loadCalendars() {
        _allCalendarsResponse.value = Result.Progress
        viewModelScope.launch {
            val calendars = calendarDataSource.loadCalendars()
// todo fill calendars data separately
            val currentCalendarsSelection = calendars.filter { it.id in (_widgetModel.value?.calendarIds ?: emptyList()) }
            _widgetModel.postValue(_widgetModel.value?.copy(calendars = currentCalendarsSelection))
            _allCalendarsResponse.postValue(Result.Calendars.Success(calendars))
        }
    }

    fun onSettingsClick() {
        _showSettingsSheet.postValue(Flag(true))
    }

    fun onWidgetChange(widget: WidgetModel) {
        _widgetModel.value = widget
    }

    fun onBackPressed() {
        if (_loadResult.value in listOf(Result.Widget.Success, Result.Widget.New) && initialWidgetModel != _widgetModel.value) {
            _showExitConfirmation.value = true
        } else {
            _finishScreen.value = FinishResult.CANCELED
        }
    }

    fun onExitConfirmed() {
        _showExitConfirmation.value = false
        _finishScreen.value = FinishResult.CANCELED
    }

    fun onExitCanceled() {
        _showExitConfirmation.value = false
    }

    fun onIntroPassed() {
        prefs.isIntroPassed = true
        _isIntroPassed.value = true
        if (_loadResult.value == Result.Widget.New) {
            _widgetModel.value = _widgetModel.value?.copy(days = WidgetModel.DAYS_DEFAULT)
        }
    }

    private suspend fun readWidgetModel(glanceId: GlanceId) {
        widgetId = glanceId.toIntId()
        val state = getAppWidgetState(ctx, PreferencesGlanceStateDefinition, glanceId)
        val widget = state.readWidgetModel(widgetId?.toLong())
        _loadResult.value = Result.Widget.Success
        _widgetModel.value = widget.also { w -> initialWidgetModel = w.copy() }
    }

    enum class FinishResult {
        OK, CANCELED
    }
}

fun GlanceId.toIntId() = (this as AppWidgetId).appWidgetId

sealed interface Result {
    object Idle : Result
    object Progress : Result
    class Error(val msg: String?) : Result

    sealed interface Widget : Result {
        object Empty : Result
        object Success : Result
        object New : Result
    }

    sealed interface Calendars : Result {
        class Success(val list: List<CalendarModel>) : Result // todo convert to object as Widget
    }
}

class Flag(val value: Boolean)
