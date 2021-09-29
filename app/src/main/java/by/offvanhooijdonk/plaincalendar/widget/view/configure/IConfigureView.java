package by.offvanhooijdonk.plaincalendar.widget.view.configure;

import java.util.List;

import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel;
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel;

public interface IConfigureView {
    void displayCalendarsDialog(List<CalendarModel> calendars);

    void onWidgetSettingsLoaded(WidgetModel widget);

    void showCalendarsLoadProgress(boolean isShow);

    void showNoWidgets(boolean isShow);

    //void onCalendarSettingsSaved();

    void notifyChangesAndFinish();
}
