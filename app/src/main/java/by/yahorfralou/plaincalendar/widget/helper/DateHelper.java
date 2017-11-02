package by.yahorfralou.plaincalendar.widget.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import by.yahorfralou.plaincalendar.widget.R;

public class DateHelper {
    public static long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

    private static final DateFormat EVENT_DATE_FORMAT = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private static final DateFormat EVENT_TIME_FORMAT = DateFormat.getTimeInstance(DateFormat.SHORT);

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DATE_ONLY = new SimpleDateFormat("d");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DATE_ONLY_LEAD_ZERO = new SimpleDateFormat("dd");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("EE");
    //private static final SimpleDateFormat SDF_TIME_HH_MM = new SimpleDateFormat("HH:mm");

    @Deprecated
    public static String formatDateOnly(Date date) {
        return formatDateOnly(date, false);
    }

    public static String formatDateOnly(Date date, boolean leadingZero) {
        if (leadingZero) {
            return SDF_DATE_ONLY_LEAD_ZERO.format(date);
        } else {
            return SDF_DATE_ONLY.format(date);
        }
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

    public static String formatEventDate(Context ctx, Date date, boolean useLabels) {
        Calendar calToday = Calendar.getInstance();
        Calendar calNextDay = Calendar.getInstance();
        calNextDay.add(Calendar.DAY_OF_MONTH, 1);
        Calendar calEvent = Calendar.getInstance();
        calEvent.setTime(date);

        if (useLabels && isSameDay(calToday, calEvent)) {
            return ctx.getString(R.string.today);
        } else if (useLabels && isSameDay(calNextDay, calEvent)) {
            return ctx.getString(R.string.tomorrow);
        } else {

            int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH |
                    (calEvent.get(Calendar.YEAR) != calToday.get(Calendar.YEAR) ? DateUtils.FORMAT_NO_YEAR : 0);
            return DateUtils.formatDateTime(ctx, date.getTime(), flags);
        }
    }

    public static String formatEventTime(Date date) {
        return EVENT_TIME_FORMAT.format(date);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
