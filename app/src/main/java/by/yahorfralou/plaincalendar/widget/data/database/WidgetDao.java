package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetCalendarBean;
import io.reactivex.Maybe;

@Dao
public interface WidgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWidget(WidgetBean widgetBean);

    @Query("SELECT * FROM widgets WHERE id = :widgetId")
    Maybe<WidgetBean> getById(long widgetId);

    @Query("DELETE FROM widget_calendar WHERE widget_id = :widgetId")
    void deleteAllWidgetCalendars(long widgetId);

    @Insert
    void saveWidgetCalendars(List<WidgetCalendarBean> list);
}
