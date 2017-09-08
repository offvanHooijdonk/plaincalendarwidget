package by.yahorfralou.plaincalendar.widget.data.calendars;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import by.yahorfralou.plaincalendar.widget.widget.EventsRemoteViewsFactory;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class CalendarsRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(LOGCAT, "RemoteService::onGetViewFactory");
        return new EventsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
