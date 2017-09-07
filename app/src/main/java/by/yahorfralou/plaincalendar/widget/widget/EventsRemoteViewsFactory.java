package by.yahorfralou.plaincalendar.widget.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarDataSource;

public class EventsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context ctx;
    private CalendarDataSource calDataSource;

    public EventsRemoteViewsFactory(Context context, Intent intent) {
        this.ctx = context;
        calDataSource = new CalendarDataSource(ctx);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        PlainCalendarWidgetApp.getAppDatabase().calendarDao().getAllSelected()
                .map(calendarBeans -> calDataSource.getEvents(calendarBeans));
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
