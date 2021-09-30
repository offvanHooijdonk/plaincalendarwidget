package by.offvanhooijdonk.plaincalendar.widget.data.database

import androidx.room.TypeConverter
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel

object Converters {
    @TypeConverter
    fun cornersToInt(corners: WidgetModel.Corners?): String? {
        return corners?.name
    }

    @TypeConverter
    fun cornersFromInt(value: String?): WidgetModel.Corners? {
        return value?.let { WidgetModel.Corners.valueOf(it) }
    }

    @TypeConverter
    fun showEndDateToString(showEndDate: WidgetModel.ShowEndDate?): String? {
        return showEndDate?.name
    }

    @TypeConverter
    fun showEndDateFromString(s: String?): WidgetModel.ShowEndDate? {
        return s?.let { WidgetModel.ShowEndDate.valueOf(it) }
    }
}
