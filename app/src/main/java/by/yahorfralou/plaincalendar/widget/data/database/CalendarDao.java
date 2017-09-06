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

    @Query("SELECT * FROM calendars WHERE is_selected = 1 ORDER BY account_name, display_name")
    Maybe<List<CalendarBean>> getAllSelected();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<CalendarBean> beans);

}
