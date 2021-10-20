package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendar.widget.data.database.WidgetDao
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ConfigureViewModel(
    private val widgetHelper: WidgetHelper,
    private val widgetDao: WidgetDao,
) : ViewModel() {
    val widgetId = ObservableField<Int?>()
    val widget = ObservableField<WidgetModel>()
    val isCreationMode = ObservableBoolean(false)
    val isShowCalendarsPick = ObservableBoolean(false)

    fun passWidgetId(id: Int?) {
        isCreationMode.set(id != null)

        widgetId.set(
            id ?: run {
                widgetHelper.getExistingWidgetsIds().firstOrNull()
            }
        )

        widgetId.get()?.let {
            loadWidgetConfig(it)
        } ?: run {
            onNoWidget()
        }
    }

    fun onPickCalendars() {
        isShowCalendarsPick.set(true)
    }

    private fun loadWidgetConfig(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            widgetDao.getById(id.toLong()).collect { w -> widget.set(w) }
        }
    }

    private fun onNoWidget() {
        TODO("Not yet implemented")
    }
}
