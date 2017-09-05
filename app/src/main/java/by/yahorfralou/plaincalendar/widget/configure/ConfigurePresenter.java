package by.yahorfralou.plaincalendar.widget.configure;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class ConfigurePresenter {

    private Context ctx;
    private IConfigureView view;

    public ConfigurePresenter(Context ctx, IConfigureView view) {
        this.ctx = ctx;
        this.view = view;
    }

    public void onCalendarsListRequested() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Log.i(LOGCAT, "No permission when trying to call for calendars!");
            // TODO output some message
            return;
        }
        List<CalendarBean> calendarList = new ArrayList<>();


        ContentResolver cr = ctx.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;

        try (Cursor cur = cr.query(uri, new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.CALENDAR_COLOR
        }, null, null, CalendarContract.Calendars.ACCOUNT_NAME + ", " + CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)) {
            while (cur != null && cur.moveToNext()) {
                CalendarBean bean = new CalendarBean();
                bean.setId(cur.getLong(cur.getColumnIndex(CalendarContract.Calendars._ID)));
                bean.setDisplayName(cur.getString(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)));
                bean.setAccountName(cur.getString(cur.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)));
                bean.setColor(cur.getInt(cur.getColumnIndex(CalendarContract.Calendars.CALENDAR_COLOR)));
            }

        } catch (Exception e) {
            // TODO handle
        }

        //PlainCalendarWidgetApp.getAppDatabase().calendarDao().insertAll(calendarList);

        view.displayCalendars(calendarList);
    }
}
