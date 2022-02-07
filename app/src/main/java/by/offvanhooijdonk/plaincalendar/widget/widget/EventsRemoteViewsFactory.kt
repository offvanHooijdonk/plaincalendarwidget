package by.offvanhooijdonk.plaincalendar.widget.widget

import android.content.Intent
import android.widget.RemoteViewsService.RemoteViewsFactory
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel
import by.offvanhooijdonk.plaincalendar.widget.data.calendars.CalendarDataSource
import by.offvanhooijdonk.plaincalendar.widget.model.EventModel
import by.offvanhooijdonk.plaincalendar.widget.app.App
import android.widget.RemoteViews
import by.offvanhooijdonk.plaincalendar.widget.R
import android.util.TypedValue
import by.offvanhooijdonk.plaincalendar.widget.helper.WidgetHelper
import android.appwidget.AppWidgetManager
import android.content.Context
import android.util.Log
import android.view.View
import by.offvanhooijdonk.plaincalendar.widget.data.database.CalendarDao
import by.offvanhooijdonk.plaincalendar.widget.data.database.WidgetDao
import org.koin.core.component.KoinComponent
import java.util.ArrayList

class EventsRemoteViewsFactory(
    private val ctx: Context,
    private val widgetDao: WidgetDao,
    private val calendarDao: CalendarDao,
    private val intent: Intent,
) : RemoteViewsFactory, KoinComponent {
    private val widgetId: Int =
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    private var widgetOptions: WidgetModel? = null
    private val calDataSource: CalendarDataSource = CalendarDataSource(ctx)
    private val eventList: MutableList<EventModel> = ArrayList()
    override fun onCreate() {
        Log.i(App.LOGCAT, "ViewsFactory::onCreate")
    }

    override fun onDataSetChanged() {
        /*Log.i(App.LOGCAT, "ViewsFactory::onDataSetChanged. Widget $widgetId")
        widgetDao.getById(widgetId.toLong())
            .flatMap { wBean ->
               calendarDao.getCalendarsForWidget(widgetId.toLong())
                    .map { calendarBeans ->
                        wBean.setCalendars(calendarBeans)
                        wBean
                    }
            }
            .map { widgetBean -> calDataSource.getEvents(widgetBean.getCalendars(), widgetBean.getDays()) }
            .subscribe { list: List<EventModel> -> displayEvents(list) }
        widgetDao.getById(widgetId.toLong())
            .subscribe { widgetBean -> widgetOptions = widgetBean }*/
    }

    private fun displayEvents(list: List<EventModel>) {
        eventList.clear()
        eventList.addAll(list)
        Log.i(App.LOGCAT, "Events List updated: " + eventList.size)
    }

    override fun getCount(): Int {
        return eventList.size
    }

    override fun getViewAt(i: Int): RemoteViews {
        /*Log.i(LOGCAT, "getViewAt: " + i);*/
        val (id, title, dateStart, dateEnd, isAllDay, eventColor, _, eventId) = eventList[i]
        // TODO make beauty
        val rv = RemoteViews(ctx.packageName, R.layout.item_event_widget)
        // TODO use defaults from PrefHelper also
        /*val eventDateText = DateHelper.formatEventDateRange(ctx, dateStart, dateEnd, isAllDay,
            (if (widgetOptions != null) widgetOptions!!.showDateTextLabel else true)!!,
            if (widgetOptions != null) widgetOptions!!.showEndDate else WidgetModel.ShowEndDate.NEVER)
        rv.setTextViewText(R.id.txtDateRange, eventDateText)
        rv.setTextViewText(R.id.txtEventTitle, title)
        if (widgetOptions != null) {
            rv.setTextColor(R.id.txtDateRange, widgetOptions!!.textColor!!)
            rv.setTextColor(R.id.txtEventTitle, widgetOptions!!.textColor!!)
            rv.setTextViewTextSize(R.id.txtDateRange, TypedValue.COMPLEX_UNIT_SP, WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_date_text, widgetOptions!!.textSizeDelta!!))
            rv.setTextViewTextSize(R.id.txtEventTitle, TypedValue.COMPLEX_UNIT_SP, WidgetHelper.riseTextSizeBy(ctx, R.dimen.widget_event_title, widgetOptions!!.textSizeDelta!!))
            if (widgetOptions!!.showEventColor!!) {
                if (eventColor != null) {
                    rv.setInt(R.id.imgColor, "setColorFilter", eventColor)
                }
                rv.setViewVisibility(R.id.imgColor, View.VISIBLE)
            } else {
                rv.setViewVisibility(R.id.imgColor, View.GONE)
            }
        }
        Log.i(App.LOGCAT, "Set intent for event: $id")
        rv.setOnClickFillInIntent(R.id.rootEventItem, WidgetHelper.createEventIntent(eventId))*/
        return rv
    }

    override fun getItemId(i: Int): Long {
        return eventList[i].id
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getLoadingView(): RemoteViews? { // todo can implement this
        return null
    }

    override fun onDestroy() {}

}
