package by.offvanhooijdonk.plaincalendar.widget.data.database

import androidx.room.Dao
import androidx.room.Insert
import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CalendarDao {
    @Query("SELECT * FROM calendars c INNER JOIN widget_calendar w ON c.id = w.calendar_id " +
           "WHERE w.widget_id = :widgetId ORDER BY c.account_name, c.primary_on_account desc, c.display_name")
    fun getCalendarsForWidget(widgetId: Long): List<CalendarModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(beans: List<CalendarModel>): LongArray
}
