package by.yahorfralou.plaincalendar.widget.configure;

import android.content.Context;
import android.util.Log;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.app.PlainCalendarWidgetApp;
import by.yahorfralou.plaincalendar.widget.data.calendars.CalendarDataSource;
import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
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
                    loadCalendarsSettings();
                });
    }

    public void loadCalendarsSettings() {
        PlainCalendarWidgetApp.getAppDatabase().calendarDao().getAllSelected()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendarBeans -> view.onCalendarSettingsLoaded(calendarBeans));
    }

    public void loadWidgetSettings() {
        PlainCalendarWidgetApp.getAppDatabase().widgetDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> view.onWidgetSettingsLoaded(list));
    }

    public void onNoWidgets() {
        view.showNoWidgets(true);
    }

    private void handleError(Throwable th) {

    }

    private void nth() {

    }

}
