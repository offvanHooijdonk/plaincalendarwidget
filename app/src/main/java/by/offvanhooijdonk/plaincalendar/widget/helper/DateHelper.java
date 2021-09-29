package by.offvanhooijdonk.plaincalendar.widget.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import by.offvanhooijdonk.plaincalendar.widget.R;
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel;

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

    public static String formatEventDateRange(Context ctx, Date dateStart, Date dateEnd, boolean isAllDay, boolean useLabels, WidgetModel.ShowEndDate endDateRule) {
        StringBuilder strDateRange = new StringBuilder();

        String textDateStart = isAllDay ?
                DateHelper.formatEventDate(ctx, dateStart, useLabels) :
                ctx.getString(R.string.format_date_plus_time,
                        DateHelper.formatEventDate(ctx, dateStart, useLabels),
                        DateHelper.formatEventTime(dateStart));
        strDateRange.append(textDateStart);

        boolean isShowEndDate = dateEnd != null &&
                (endDateRule == WidgetModel.ShowEndDate.ALWAYS || endDateRule == WidgetModel.ShowEndDate.MORE_THAN_DAY && !isSameDay(dateStart, dateEnd));

        if (isShowEndDate) {
            String textDateEnd = isSameDay(dateStart, dateEnd) ?
                    DateHelper.formatEventTime(dateEnd) :
                    ctx.getString(R.string.format_date_plus_time,
                            DateHelper.formatEventDate(ctx, dateEnd, useLabels),
                            (isAllDay ? "" : DateHelper.formatEventTime(dateEnd)));

            strDateRange.append(ctx.getString(R.string.date_range_separator)).append(textDateEnd);
        }

        return strDateRange.toString();
    }

    private static String formatEventDate(Context ctx, Date date, boolean useLabels) {
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

    private static String formatEventTime(Date date) {
        return EVENT_TIME_FORMAT.format(date);
    }

    private static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return isSameDay(cal1, cal2);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
