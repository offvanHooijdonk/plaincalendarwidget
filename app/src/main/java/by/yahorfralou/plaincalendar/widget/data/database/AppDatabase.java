package by.yahorfralou.plaincalendar.widget.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

@Database(entities = {CalendarBean.class, WidgetBean.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "plain-calendar";
    public abstract CalendarDao calendarDao();
    public abstract WidgetDao widgetDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE widgets (id INTEGER, PRIMARY KEY(id))");
        }
    };

    public static AppDatabase buildDatabase(Context ctx) {
        return Room.databaseBuilder(ctx, AppDatabase.class, DB_NAME).addMigrations(MIGRATION_1_2).build();
    }
}
