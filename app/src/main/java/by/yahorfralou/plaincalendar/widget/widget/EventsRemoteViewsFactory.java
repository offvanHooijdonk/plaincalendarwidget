package by.yahorfralou.plaincalendar.widget.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.R;
import by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarDataSource;
import by.yahorfralou.plaincalendar.widget.helper.DateHelper;
import by.yahorfralou.plaincalendar.widget.helper.WidgetHelper;
import by.yahorfralou.plaincalendar.widget.model.EventBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class EventsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final int widgetId;
    private WidgetBean widgetOptions;
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
        Log.i(LOGCAT, "ViewsFactory::onDataSetChanged. Widget " + widgetId);
        PlainCalendarWidgetApp.getAppDatabase().widgetDao().getById(widgetId)
                .flatMap(wBean -> PlainCalendarWidgetApp.getAppDatabase().calendarDao().getCalendarsForWidget(widgetId)
                        .map(calendarBeans -> {
                            wBean.setCalendars(calendarBeans);
                            return wBean;
                        }))
                .map(widgetBean -> calDataSource.getEvents(widgetBean.getCalendars(), widgetBean.getDays()))
                .subscribe(this::displayEvents);

        PlainCalendarWidgetApp.getAppDatabase().widgetDao().getById(widgetId)
                .subscribe(widgetBean -> this.widgetOptions = widgetBean);
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
        /*Log.i(LOGCAT, "getViewAt: " + i);*/
        EventBean event = eventList.get(i);
// TODO make beauty
        RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.item_event_widget);
        // TODO use defaults from PrefHelper also
        String eventDateText = DateHelper.formatEventDateRange(ctx, event.getDateStart(), event.getDateStart(), event.isAllDay(),
                widgetOptions != null ? widgetOptions.getShowDateTextLabel() : true,
                widgetOptions != null ? widgetOptions.getShowEndDate() : WidgetBean.ShowEndDate.NEVER);

        rv.setTextViewText(R.id.txtDateRange, eventDateText);
        rv.setTextViewText(R.id.txtEventTitle, event.getTitle());

        if (widgetOptions != null) {
            rv.setTextColor(R.id.txtDateRange, widgetOptions.getTextColor());
            rv.setTextColor(R.id.txtEventTitle, widgetOptions.getTextColor());

            rv.setTextViewTextSize(R.id.txtDateRange, TypedValue.COMPLEX_UNIT_SP, WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_date_text, widgetOptions.getTextSizeDelta()));
            rv.setTextViewTextSize(R.id.txtEventTitle, TypedValue.COMPLEX_UNIT_SP, WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_event_title, widgetOptions.getTextSizeDelta()));

            if (widgetOptions.getShowEventColor()) {
                if (event.getEventColor() != null) {
                    rv.setInt(R.id.imgColor, "setColorFilter", event.getEventColor());
                }
                rv.setViewVisibility(R.id.imgColor, View.VISIBLE);
            } else {
                rv.setViewVisibility(R.id.imgColor, View.GONE);
            }
        }



        Log.i(LOGCAT, "Set intent for event: " + event.getId());
        rv.setOnClickFillInIntent(R.id.rootEventItem, WidgetHelper.createEventIntent(event.getEventId()));

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
