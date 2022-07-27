package by.offvanhooijdonk.plaincalendarv2.widget.ui.configure

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.offvanhooijdonk.plaincalendar.widget.data.database.WidgetDao
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigureViewModel(
    private val widgetDao: WidgetDao,
) : ViewModel() {
    private val _widgetResponse = MutableLiveData<Result>(Result.Idle)
    val widgetResponse: LiveData<Result> = _widgetResponse

    fun passWidgetId(id: Int?) {
        id?.let {
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
}

sealed interface Result {
    object Idle : Result
    object Progress : Result
    class Error(val msg: String?) : Result
    class Success(val data: WidgetModel) : Result
    object Empty : Result
}
