package by.yahorfralou.plaincalendar.widget.helper;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DATE_ONLY = new SimpleDateFormat("d");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("EE");

    public static String formatDateOnly(Date date) {
        return SDF_DATE_ONLY.format(date);
    }

    public static String formatDay(Date date) {
        return SDF_DAY.format(date);
    }
}
