package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import io.reactivex.Maybe;

@Dao
public interface CalendarDao {
    @Query("SELECT * FROM calendars c INNER JOIN widget_calendar w ON c.id = w.calendar_id " +
            "WHERE w.widget_id = :widgetId ORDER BY c.account_name, c.primary_on_account desc, c.display_name")
    Maybe<List<CalendarBean>> getCalendarsForWidget(long widgetId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<CalendarBean> beans);

}
