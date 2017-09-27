package by.yahorfralou.plaincalendar.widget.app;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import by.yahorfralou.plaincalendar.widget.data.database.AppDatabase;

public class PlainCalendarWidgetApp extends Application {
    public static final String LOGCAT = "=== PCW ===";
    public static final String LOG_FILE_DIR = "plainCalendarWidget/log";
    public static final String LOG_FILE_NAME = "log.txt";


    private static AppDatabase APP_DB;

    @Override
    public void onCreate() {
        super.onCreate();

        initLogcatToFile();

        APP_DB = AppDatabase.buildDatabase(getApplicationContext());
        Log.i(LOGCAT, "DB initialized");
    }

    public static AppDatabase getAppDatabase() {
        return APP_DB;
    }

    private void initLogcatToFile() {
        String rootPath = getExternalFilesDir(null).getAbsolutePath();
        Log.i(LOGCAT, "Root path: " + rootPath);

        File rootDir = getExternalFilesDir(null);
        File logFile = new File(rootDir, LOG_FILE_NAME);

        try {
            Process process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -f " + logFile);
        } catch (IOException e) {
            Log.e(LOGCAT, "Error trying to execute commends for logging into a file", e);
        }
    }
}
