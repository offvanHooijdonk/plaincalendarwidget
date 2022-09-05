package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendarv2.widget.data.CalendarDataSource
import by.offvanhooijdonk.plaincalendarv2.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendarv2.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.PlainGlanceWidget
import by.offvanhooijdonk.plaincalendarv2.widget.glance.prefs.WidgetPrefsReaderWriter
import by.offvanhooijdonk.plaincalendarv2.widget.model.DummyWidget
import kotlinx.coroutines.launch

class ConfigureViewModel(
    private val ctx: Context,
    /*private val widgetDao: WidgetDao,*/
    private val calendarDataSource: CalendarDataSource,
) : ViewModel() {
    private val _loadResult = MutableLiveData<Result>(Result.Idle)
    val loadResult: LiveData<Result> = _loadResult

    private val _widgetModel = MutableLiveData(DummyWidget)
    val widgetModel: LiveData<WidgetModel> = _widgetModel

    private val _allCalendarsResponse = MutableLiveData<Result>(Result.Idle)
    val calendarsResponse: LiveData<Result> = _allCalendarsResponse

    private val _finishScreen = MutableLiveData(false)
    val finishScreen: LiveData<Boolean> = _finishScreen

    private val _showSettingsSheet = MutableLiveData(Flag(false))
    val showSettingsSheet: LiveData<Flag> = _showSettingsSheet

    var widgetId: Int? = null

    fun passWidgetId(id: Int?) {
        widgetId = id
        widgetId?.let {
            _loadResult.postValue(Result.Widget.New)
            _widgetModel.postValue(WidgetModel.createDefault(it.toLong()))
        } ?: run {
            // todo check user has active widgets and show edit screen
            _loadResult.postValue(Result.Widget.Success)
            _widgetModel.postValue(WidgetModel.createDefault())
        }
    }

    fun updateWidget() {
        viewModelScope.launch { // todo add ability to call update from other places?
            var updateGlanceId: GlanceId? = null
            GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).forEach { glanceId ->
                if (glanceId.toIntId() == widgetId) {
                    updateGlanceId = glanceId
                    updateAppWidgetState(ctx, glanceId) { prefs ->
                        _widgetModel.value?.let { WidgetPrefsReaderWriter.writeToPrefs(prefs, it) }
                    }
                }
            }
            updateGlanceId?.let { PlainGlanceWidget().update(ctx, it) }
        }

        _finishScreen.value = true
    }

    fun loadCalendars() {
        _allCalendarsResponse.value = Result.Progress
        viewModelScope.launch {
            val calendars = calendarDataSource.loadCalendars()
            _allCalendarsResponse.postValue(Result.Calendars.Success(calendars))
        }
    }

    fun onSettingsClick() {
        _showSettingsSheet.postValue(Flag(true))
    }

    fun onWidgetChange(widget: WidgetModel) {
        _widgetModel.value = widget
    }
}

// current implementation while now match between GlanceId and WidgetId can be made
fun GlanceId.toIntId() =
    this.toString().let {
        it.substring(it.indexOfFirst { ch -> ch.isDigit() }, it.indexOfLast { ch -> ch.isDigit() } + 1).toInt()
    }

sealed interface Result { // todo remove cause configuration will be loaded from Prefs by Glance?
    object Idle : Result
    object Progress : Result
    class Error(val msg: String?) : Result

    sealed interface Widget : Result {
        object Success : Result
        object New : Result
    }

    sealed interface Calendars : Result {
        class Success(val list: List<CalendarModel>) : Result // todo convert to object as Widget
    }
}

class Flag(val value: Boolean)
