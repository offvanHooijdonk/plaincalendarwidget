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

@Database(entities = {CalendarBean.class, WidgetBean.class, WidgetCalendarBean.class}, version = 3)
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE calendars ADD COLUMN widget_id INTEGER DEFAULT null;");
            database.execSQL("ALTER TABLE widgets ADD COLUMN back_color INTEGER;");
            database.execSQL("ALTER TABLE widgets ADD COLUMN date_color INTEGER;");
        }
    };

    public static AppDatabase buildDatabase(Context ctx) {
        return Room.databaseBuilder(ctx, AppDatabase.class, DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build();
    }
}
