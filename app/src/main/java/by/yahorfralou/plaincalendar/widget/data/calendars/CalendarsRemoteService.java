package by.yahorfralou.plaincalendar.widget.data.calendars;

import android.content.Intent;
import android.widget.RemoteViewsService;

import by.yahorfralou.plaincalendar.widget.widget.EventsRemoteViewsFactory;

public class CalendarsRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new EventsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
