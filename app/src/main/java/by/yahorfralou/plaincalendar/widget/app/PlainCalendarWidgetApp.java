package by.yahorfralou.plaincalendar.widget.app;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import by.yahorfralou.plaincalendar.widget.data.database.AppDatabase;

public class PlainCalendarWidgetApp extends Application {
    public static final String LOGCAT = "=== PCW ===";

    public static final String DB_NAME = "plain-calendar";
    private static AppDatabase APP_DB;

    @Override
    public void onCreate() {
        super.onCreate();

        APP_DB = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, DB_NAME).build();
        Log.i(LOGCAT, "DB initialized");
    }

    public static AppDatabase getAppDatabase() {
        return APP_DB;
    }
}
