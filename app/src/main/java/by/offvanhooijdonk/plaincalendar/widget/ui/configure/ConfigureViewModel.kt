package by.offvanhooijdonk.plaincalendar.widget.ui.configure

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import by.offvanhooijdonk.plaincalendar.widget.data.database.WidgetDao
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper

class ConfigureViewModel(
    private val widgetHelper: WidgetHelper,
    private val widgetDao: WidgetDao,
) : ViewModel() {
    val widgetId = ObservableField<Int?>()
    val isCreationMode = ObservableBoolean(false)

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

    private fun loadWidgetConfig(id: Int) {
        widgetDao.getById(id.toLong())
    }

    private fun onNoWidget() {
        TODO("Not yet implemented")
    }
}
