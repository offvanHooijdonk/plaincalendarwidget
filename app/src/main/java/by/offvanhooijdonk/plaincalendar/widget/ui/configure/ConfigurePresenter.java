package by.offvanhooijdonk.plaincalendar.widget.ui.configure;

@Deprecated
public class ConfigurePresenter {

    /*private Context ctx;
    private IConfigureView view;

    public ConfigurePresenter(Context ctx, IConfigureView view) {
        this.ctx = ctx;
        this.view = view;

    }

    // TODO handle errors everywhere
    public void onPickCalendarsRequested() {
        view.showCalendarsLoadProgress(true);
        new CalendarDataSource(ctx).requestCalendarList()
                .map(calendarBeans -> {
                    App.getAppDatabase().calendarDao().insertAll(calendarBeans);
                    return calendarBeans;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(calendarBeans -> {
                    // FIXME save calendars settings before returning to Activity
                    view.showCalendarsLoadProgress(false);
                    view.displayCalendarsDialog(calendarBeans);
                });
    }

    public void loadWidgetSettings(long widgetId) {
        App.getAppDatabase().widgetDao().getById(widgetId)
                .flatMap(widgetBean -> App.getAppDatabase().calendarDao().getCalendarsForWidget(widgetId)
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
        Log.e(LOGCAT, "Error in ConfigPresenter! " + th.getMessage(), th);
    }

    private void nth() {

    }

    public void onApplySettings(WidgetModel widgetModel) {
        List<WidgetCalendarModel> widgetCalendars = new ArrayList<>();
        *//*Log.i(LOGCAT, "Apply widget " + widgetBean);
        Log.i(LOGCAT, "Apply with calendars " + widgetBean.getCalendars());*//*
        for (CalendarModel cal : widgetModel.getCalendars()) {
            widgetCalendars.add(new WidgetCalendarModel(widgetModel.getId(), cal.getId()));
        }

        Maybe.fromAction(() -> {
            //PlainCalendarWidgetApp.getAppDatabase().beginTransaction();
            App.getAppDatabase().widgetDao().saveWidget(widgetModel);
            App.getAppDatabase().widgetDao().deleteAllWidgetCalendars(widgetModel.getId());
            App.getAppDatabase().widgetDao().saveWidgetCalendars(widgetCalendars);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> nth(), this::handleError, () -> {
                    view.notifyChangesAndFinish();
                });
    }*/
}
