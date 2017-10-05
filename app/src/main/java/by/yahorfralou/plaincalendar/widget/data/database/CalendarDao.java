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

    @Query("SELECT * FROM calendars WHERE is_selected = 1 ORDER BY account_name, primary_on_account desc, display_name")
    Maybe<List<CalendarBean>> getAllSelected();

    @Query("SELECT * FROM calendars c JOIN widget_calendar w WHERE w.widget_id = :widgetId AND c.is_selected = 1 ORDER BY c.account_name, c.primary_on_account desc, c.display_name")
    Maybe<List<CalendarBean>> getCalendarsForWidget(long widgetId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<CalendarBean> beans);

}
