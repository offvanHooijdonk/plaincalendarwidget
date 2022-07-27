package by.offvanhooijdonk.plaincalendar.widget.data.database

import androidx.room.TypeConverter
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel

class Converters {
    @TypeConverter
    fun showEndDateToString(showEndDate: WidgetModel.ShowEndDate?): String? {
        return showEndDate?.name
    }

    @TypeConverter
    fun showEndDateFromString(s: String?): WidgetModel.ShowEndDate? {
        return s?.let { WidgetModel.ShowEndDate.valueOf(it) }
    }
}
