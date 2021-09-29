package by.offvanhooijdonk.plaincalendar.widget.data.calendars;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import by.offvanhooijdonk.plaincalendar.widget.widget.EventsRemoteViewsFactory;

import static by.offvanhooijdonk.plaincalendar.widget.app.App.LOGCAT;

public class CalendarsRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i(LOGCAT, "RemoteService::onGetViewFactory");
        return new EventsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
