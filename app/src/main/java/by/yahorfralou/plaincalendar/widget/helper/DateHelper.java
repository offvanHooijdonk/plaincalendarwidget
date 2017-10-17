package by.yahorfralou.plaincalendar.widget.helper;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    private static final DateFormat EVENT_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    private static final DateFormat EVENT_TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DATE_ONLY = new SimpleDateFormat("d");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("EE");
    //private static final SimpleDateFormat SDF_TIME_HH_MM = new SimpleDateFormat("HH:mm");

    public static String formatDateOnly(Date date) {
        return SDF_DATE_ONLY.format(date);
    }

    public static String formatDay(Date date) {
        return SDF_DAY.format(date);
    }

    /*public static String formatTimeHourMin(Date date) {
        return SDF_TIME_HH_MM.format(date);
    }*/

    public static long getClosestMidnightMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        //calendar.add(Calendar.MINUTE, 1);

        return calendar.getTimeInMillis();
    }

    public static String formatEventDate(Date date) {
        return EVENT_DATE_FORMAT.format(date);
    }

    public static String formatEventTime(Date date) {
        return EVENT_TIME_FORMAT.format(date);
    }
}
