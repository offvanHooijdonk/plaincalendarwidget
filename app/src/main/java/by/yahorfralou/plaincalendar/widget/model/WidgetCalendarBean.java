package by.yahorfralou.plaincalendar.widget.model;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import java.util.Objects;

@Entity(tableName = "widget_calendar", primaryKeys = {"widget_id", "calendar_id"})
public class WidgetCalendarBean {
    @ColumnInfo(name = "widget_id")
    private long widgetId;
    @ColumnInfo(name = "calendar_id")
    private long calendarId;

    public long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetId, calendarId);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof WidgetCalendarBean && widgetId == ((WidgetCalendarBean) obj).widgetId && calendarId == ((WidgetCalendarBean) obj).calendarId;
    }
}
