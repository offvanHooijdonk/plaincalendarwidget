package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetCalendarBean;

@Database(entities = {CalendarBean.class, WidgetBean.class, WidgetCalendarBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "plain-calendar-widget";
    public abstract CalendarDao calendarDao();
    public abstract WidgetDao widgetDao();

    public static AppDatabase buildDatabase(Context ctx) {
        return Room.databaseBuilder(ctx, AppDatabase.class, DB_NAME)
                .build();
    }
}
