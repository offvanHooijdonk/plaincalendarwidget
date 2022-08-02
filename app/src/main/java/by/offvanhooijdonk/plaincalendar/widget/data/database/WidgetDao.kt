package by.offvanhooijdonk.plaincalendar.widget.data.database

import by.offvanhooijdonk.plaincalendar.widget.model.WidgetCalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import kotlinx.coroutines.flow.Flow

//@Dao
interface WidgetDao {
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWidget(widgetModel: WidgetModel)

    //@Query("SELECT * FROM widgets WHERE id = :widgetId")
    fun getById(widgetId: Long): Flow<WidgetModel?>

    //@Query("DELETE FROM widget_calendar WHERE widget_id = :widgetId")
    fun deleteAllWidgetCalendars(widgetId: Long)

    //@Insert
    fun saveWidgetCalendars(list: List<WidgetCalendarModel>)
}
