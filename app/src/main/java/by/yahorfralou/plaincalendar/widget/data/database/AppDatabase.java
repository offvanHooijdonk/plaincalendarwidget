package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetCalendarBean;

@Database(entities = {CalendarBean.class, WidgetBean.class, WidgetCalendarBean.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "plain-calendar-widget";
    public abstract CalendarDao calendarDao();
    public abstract WidgetDao widgetDao();

    private static Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE widgets ADD COLUMN opacity INTEGER;");
        }
    };

    public static AppDatabase buildDatabase(Context ctx) {
        return Room.databaseBuilder(ctx, AppDatabase.class, DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
    }
}
