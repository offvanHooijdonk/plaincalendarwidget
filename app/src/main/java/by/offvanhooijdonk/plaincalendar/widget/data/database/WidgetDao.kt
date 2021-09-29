package by.offvanhooijdonk.plaincalendar.widget.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetCalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveWidget(widgetModel: WidgetModel)

    @Query("SELECT * FROM widgets WHERE id = :widgetId")
    suspend fun getById(widgetId: Long): WidgetModel?

    @Query("DELETE FROM widget_calendar WHERE widget_id = :widgetId")
    suspend fun deleteAllWidgetCalendars(widgetId: Long)

    @Insert
    suspend fun saveWidgetCalendars(list: List<WidgetCalendarModel>)
}
