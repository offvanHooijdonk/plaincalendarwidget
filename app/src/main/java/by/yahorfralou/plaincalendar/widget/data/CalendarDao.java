package by.yahorfralou.plaincalendar.widget.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;

@Dao
public interface CalendarDao {

    @Query("SELECT * from calendars")
    List<CalendarBean> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CalendarBean> beans);

}
