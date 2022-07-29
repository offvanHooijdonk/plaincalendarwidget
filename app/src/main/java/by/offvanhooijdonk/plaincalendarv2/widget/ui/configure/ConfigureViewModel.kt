package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendar.widget.data.database.WidgetDao
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendarv2.widget.glance.PlainGlanceWidget
import by.offvanhooijdonk.plaincalendarv2.widget.glance.WidgetPrefsKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigureViewModel(
    private val ctx: Context,
    private val widgetDao: WidgetDao,
) : ViewModel() {
    private val _widgetResponse = MutableLiveData<Result>(Result.Idle)
    val widgetResponse: LiveData<Result> = _widgetResponse

    private val _finishScreen = MutableLiveData(false)
    val finishScreen: LiveData<Boolean> = _finishScreen

    var widgetId: Int? = null

    fun passWidgetId(id: Int?) {
        widgetId = id
        widgetId?.let {
            loadWidgetConfig(it)
        } ?: run {
            _widgetResponse.postValue(Result.Empty)
        }
    }

    private fun loadWidgetConfig(id: Int) {
        _widgetResponse.value = Result.Progress

        viewModelScope.launch(Dispatchers.IO) {
            widgetDao.getById(id.toLong()).collect { widget ->
                _widgetResponse.postValue(if (widget != null) Result.Success(widget) else Result.Empty)
            }
        }
    }

    fun updateWidget(widgetModel: WidgetModel) {
        // todo update settings
        viewModelScope.launch {
            var updateGlanceId: GlanceId? = null
            GlanceAppWidgetManager(ctx).getGlanceIds(PlainGlanceWidget::class.java).forEach { glanceId ->
                if (glanceId.toIntId() == widgetId) {
                    updateGlanceId = glanceId
                    updateAppWidgetState(ctx, glanceId) { prefs ->
                        WidgetPrefsKeys.writeToPrefs(prefs, widgetModel)
                    }
                }
            }
            updateGlanceId?.let { PlainGlanceWidget().update(ctx, it) }
        }

        _finishScreen.value = true
    }
}

// current implementation while now match between GlanceId and WidgetId can be made
fun GlanceId.toIntId() =
    this.toString().let {
        it.substring(it.indexOfFirst { ch -> ch.isDigit() }, it.indexOfLast { ch -> ch.isDigit() } + 1).toInt()
    }

sealed interface Result {
    object Idle : Result
    object Progress : Result
    class Error(val msg: String?) : Result
    class Success(val data: WidgetModel) : Result
    object Empty : Result
}
