package by.yahorfralou.plaincalendar.widget.app;

import android.app.Application;
import android.util.Log;

import by.yahorfralou.plaincalendar.widget.data.database.AppDatabase;

public class PlainCalendarWidgetApp extends Application {
    public static final String LOGCAT = "=== PCW ===";


    private static AppDatabase APP_DB;

    @Override
    public void onCreate() {
        super.onCreate();

        APP_DB = AppDatabase.buildDatabase(getApplicationContext());
        Log.i(LOGCAT, "DB initialized");
    }

    public static AppDatabase getAppDatabase() {
        return APP_DB;
    }
}
