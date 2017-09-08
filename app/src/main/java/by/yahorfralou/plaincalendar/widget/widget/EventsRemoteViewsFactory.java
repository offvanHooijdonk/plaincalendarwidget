package by.yahorfralou.plaincalendar.widget.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarDataSource;
import by.yahorfralou.plaincalendar.widget.model.EventBean;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class EventsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    private final int widgetId;
    private Context ctx;
    private CalendarDataSource calDataSource;
    private List<EventBean> eventList = new ArrayList<>();

    public EventsRemoteViewsFactory(Context context, Intent intent) {
        this.ctx = context;
        calDataSource = new CalendarDataSource(ctx);

        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.i(LOGCAT, "ViewsFactory::onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Log.i(LOGCAT, "ViewsFactory::onDataSetChanged");
        PlainCalendarWidgetApp.getAppDatabase().calendarDao().getAllSelected()
                .map(calendarBeans -> calDataSource.getEvents(calendarBeans))
                .subscribe(this::displayEvents);
    }

    private void displayEvents(List<EventBean> list) {
        eventList.clear();
        eventList.addAll(list);
        Log.i(LOGCAT, "Events List updated: " + eventList.size());
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        Log.i(LOGCAT, "getViewAt: " + i);
        EventBean event = eventList.get(i);
// TODO make beauty
        RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.item_event_widget);
        rv.setTextViewText(R.id.txtDateRange, String.format("%s", event.isAllDay() ? "All Day" : DATE_FORMAT.format(event.getDateStart())));
        rv.setTextViewText(R.id.txtEventTitle, event.getTitle());

        return rv;
    }


    @Override
    public long getItemId(int i) {
        return eventList.get(i).getId();
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
