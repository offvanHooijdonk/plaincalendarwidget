package by.yahorfralou.plaincalendar.widget.view.configure;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarDataSource;
import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetCalendarBean;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp.LOGCAT;

public class ConfigurePresenter {

    private Context ctx;
    private IConfigureView view;

    public ConfigurePresenter(Context ctx, IConfigureView view) {
        this.ctx = ctx;
        this.view = view;

    }

    // TODO handle errors everywhere
    public void onPickCalendarsRequested() {
        view.showCalendarsLoadProgress(true);
        new CalendarDataSource(ctx).requestCalendarList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendarBeans -> {
                    // FIXME save calendars settings before returning to Activity
                    view.showCalendarsLoadProgress(false);
                    view.displayCalendarsDialog(calendarBeans);
                });
    }

    public void updateCalendarsSettings(List<CalendarBean> calendars) {
        Maybe.fromAction(() -> PlainCalendarWidgetApp.getAppDatabase().calendarDao().insertAll(calendars))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> nth(), this::handleError, () -> {
                    Log.d(LOGCAT, "Calendars settings saved");
                    view.onCalendarSettingsSaved();
                });
    }

    /*private void loadCalendarsSettings() {
        PlainCalendarWidgetApp.getAppDatabase().calendarDao().getAllSelected()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendarBeans -> view.onCalendarSettingsSaved(calendarBeans));
    }*/

    public void loadWidgetSettings(long widgetId) {
        PlainCalendarWidgetApp.getAppDatabase().widgetDao().getById(widgetId)
                .flatMap(widgetBean -> PlainCalendarWidgetApp.getAppDatabase().calendarDao().getCalendarsForWidget(widgetId)
                        .map(calendarBeans -> {
                            widgetBean.setCalendars(calendarBeans);
                            return widgetBean;
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(widgetBean -> view.onWidgetSettingsLoaded(widgetBean));
    }

    public void onNoWidgets() {
        view.showNoWidgets(true);
    }

    private void handleError(Throwable th) {

    }

    private void nth() {

    }

    public void onApplySettings(WidgetBean widgetBean) {
        List<WidgetCalendarBean> widgetCalendars = new ArrayList<>();
        for (CalendarBean cal : widgetBean.getCalendars()) {
            widgetCalendars.add(new WidgetCalendarBean(widgetBean.getId(), cal.getId()));
        }


        Maybe.fromAction(() -> {
                    PlainCalendarWidgetApp.getAppDatabase().beginTransaction();
                    PlainCalendarWidgetApp.getAppDatabase().widgetDao().saveWidget(widgetBean);
                })
                .map(o -> {
                    PlainCalendarWidgetApp.getAppDatabase().widgetDao().deleteAllWidgetCalendars(widgetBean.getId());
                    return o;
                })
                .map(o -> {
                    PlainCalendarWidgetApp.getAppDatabase().widgetDao().saveWidgetCalendars(widgetCalendars);
                    return o;
                })
                .map(o -> {
                    PlainCalendarWidgetApp.getAppDatabase().endTransaction();
                    return o;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> nth(), this::handleError, () -> {
                    view.notifyChangesAndFinish();
                });
    }
}
