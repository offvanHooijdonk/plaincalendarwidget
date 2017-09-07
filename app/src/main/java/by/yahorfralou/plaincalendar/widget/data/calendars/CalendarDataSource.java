package by.yahorfralou.plaincalendar.widget.data.calendars;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.EventBean;
import io.reactivex.Maybe;

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
        } catch (Exception e) {
            // TODO handle
        }

        return calendarList;
    }

    public List<EventBean> getEvents(List<CalendarBean> calendars) {
        List<EventBean> eventBeans = new ArrayList<>();
        ContentResolver cr = ctx.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;

        try(Cursor cur = prepareEventsCursor(cr, uri)) {

        } catch (Exception e) {

        }

        return eventBeans;
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

    private Cursor prepareEventsCursor(ContentResolver cr, Uri uri) {
        return cr.query(uri, new String[]{}, "", new String[]{}, null);
    }

}
