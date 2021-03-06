package by.yahorfralou.plaincalendar.widget.data.calendars;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.EventBean;
import io.reactivex.Maybe;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarDataSource {
    private static final int BOOLEAN_TRUE = 1;
    private Context ctx;

    public CalendarDataSource(Context context) {
        this.ctx = context;
    }

    public Maybe<List<CalendarBean>> requestCalendarList() {
        return Maybe.fromCallable(this::loadCalendars);
    }

    private List<CalendarBean> loadCalendars() {
        List<CalendarBean> calendarList = new ArrayList<>();

        ContentResolver cr = ctx.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        try (Cursor cur = prepareCalendarsCursor(cr, uri)) {
            while (cur != null && cur.moveToNext()) {
                CalendarBean bean = new CalendarBean();
                bean.setId(cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID)));
                bean.setDisplayName(cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)));
                bean.setAccountName(cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)));
                bean.setColor(cur.getInt(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR)));
                bean.setPrimaryOnAccount(cur.getInt(cur.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)) == BOOLEAN_TRUE);

                calendarList.add(bean);
            }

        } catch (SecurityException e) {
            // TODO handle
            Log.e(LOGCAT, "Security exception accessing Calendars list", e);
        } catch (Exception e) {
            // TODO handle
            Log.e(LOGCAT, "Error getting Calendars", e);
        }

        return calendarList;
    }

    public List<EventBean> getEvents(List<CalendarBean> calendars, int daysAhead) {
        List<EventBean> eventBeans = new ArrayList<>();
        ContentResolver cr = ctx.getContentResolver();
        Log.i(LOGCAT, "Getting Events for " + calendars.size() + " calendars");

        try (Cursor cur = prepareEventsCursor(cr, calendars, daysAhead)) {
            while (cur != null && cur.moveToNext()) {
                EventBean event = new EventBean();

                event.setId(cur.getLong(cur.getColumnIndex(CalendarContract.Instances._ID)));
                event.setEventId(cur.getLong(cur.getColumnIndex(CalendarContract.Instances.EVENT_ID)));
                event.setTitle(cur.getString(cur.getColumnIndex(CalendarContract.Instances.TITLE)));
                event.setDateStart(new Date(cur.getLong(cur.getColumnIndex(CalendarContract.Instances.BEGIN))));
                event.setDateEnd(new Date(cur.getLong(cur.getColumnIndex(CalendarContract.Instances.END))));
                event.setAllDay(cur.getInt(cur.getColumnIndex(CalendarContract.Instances.ALL_DAY)) == BOOLEAN_TRUE);
                event.setEventColor(cur.getInt(cur.getColumnIndex(CalendarContract.Instances.DISPLAY_COLOR)));
                event.setCalendarId(cur.getLong(cur.getColumnIndex(CalendarContract.Instances.CALENDAR_ID)));
                Log.i(LOGCAT, "Event read: " + event);

                eventBeans.add(event);
            }
        } catch (Exception e) {
            // TODO handle
            Log.e(LOGCAT, "Error getting Events", e);
        }

        return eventBeans;
    }

    public static Uri makeEventsObservationUri() {
        return makeEventsUriForDates(new Date(), null);
    }

    private static Uri makeEventsUriForDates(Date dateFrom, Date dateTo) {
        Uri uri = CalendarContract.Instances.CONTENT_URI;
        if (dateFrom != null) {
            Uri.Builder uriBuilder = uri.buildUpon().appendPath(String.valueOf(dateFrom.getTime()));
            if (dateTo != null) {
                uriBuilder.appendPath(String.valueOf(dateTo.getTime()));
            }

            uri = uriBuilder.build();
        }

        return uri;
    }

    private Cursor prepareCalendarsCursor(ContentResolver cr, Uri uri) {
        return cr.query(uri, new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        CalendarContract.Calendars.CALENDAR_COLOR,
                        CalendarContract.Calendars.IS_PRIMARY
                }, null, null,
                CalendarContract.Calendars.ACCOUNT_NAME + ", " + CalendarContract.Calendars.IS_PRIMARY + " desc, " + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME);
    }

    private static Date prepareDateTo(Date dateFrom, int daysAhead) {
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateFrom);
        calendarTo.add(Calendar.DAY_OF_MONTH, daysAhead);

        calendarTo.set(Calendar.HOUR_OF_DAY, 0);
        calendarTo.set(Calendar.MINUTE, 0);
        calendarTo.set(Calendar.SECOND, 0);
        calendarTo.add(Calendar.SECOND, -1);

        return calendarTo.getTime();
    }

    private Cursor prepareEventsCursor(ContentResolver cr, List<CalendarBean> calendars, int daysAhead) {
        Date dateFrom = new Date();
        Date dateTo = prepareDateTo(dateFrom, daysAhead);

        return cr.query(makeEventsUriForDates(dateFrom, dateTo),
                new String[]{
                        CalendarContract.Instances._ID,
                        CalendarContract.Instances.EVENT_ID,
                        CalendarContract.Instances.TITLE,
                        CalendarContract.Instances.BEGIN,
                        CalendarContract.Instances.END,
                        CalendarContract.Instances.ALL_DAY,
                        CalendarContract.Instances.DISPLAY_COLOR,
                        CalendarContract.Instances.CALENDAR_ID
                }, CalendarContract.Instances.CALENDAR_ID + " IN (" + prepareMultipleSubsPlaceholders(calendars.size()) + ")",
                prepareEventsArgs(calendars),
                CalendarContract.Instances.BEGIN + ", " + CalendarContract.Instances.END + ", " + CalendarContract.Instances.TITLE);
    }

    private String prepareMultipleSubsPlaceholders(int n) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            stringBuilder.append("?");
            if (i < n - 1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }

    private String[] prepareEventsArgs(List<CalendarBean> calendars) {
        String[] args = new String[calendars.size()];
        int i = 0;
        for (CalendarBean bean : calendars) {
            args[i] = String.valueOf(bean.getId());
            i++;
        }

        return args;
    }

}
