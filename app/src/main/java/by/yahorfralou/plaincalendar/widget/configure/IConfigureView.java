package by.yahorfralou.plaincalendar.widget.configure;

import java.util.List;

import by.yahorfralou.plaincalendar.widget.model.CalendarBean;

public interface IConfigureView {
    void displayCalendarsDialog(List<CalendarBean> calendars);

    void onCalendarSettingsLoaded(List<CalendarBean> list);

    void showCalendarsLoadProgress(boolean isShow);
}
