package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;

@Database(entities = {CalendarBean.class/*, WidgetBean.class*/}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CalendarDao calendarDao();
    /*public abstract WidgetDao widgetDao();*/
}
