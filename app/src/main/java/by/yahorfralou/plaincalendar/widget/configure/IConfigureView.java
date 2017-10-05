package by.yahorfralou.plaincalendar.widget.configure;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;
import by.yahorfralou.plaincalendar.widget.model.WidgetBean;

public interface IConfigureView {
    void displayCalendarsDialog(List<CalendarBean> calendars);

    void onWidgetSettingsLoaded(WidgetBean widget);

    void showCalendarsLoadProgress(boolean isShow);

    void showNoWidgets(boolean isShow);
}
