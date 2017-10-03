package by.yahorfralou.plaincalendar.widget.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "widgets")
public class WidgetBean {
    @PrimaryKey
    private int id;
    @Ignore
    private List<CalendarBean> calendars;
    @ColumnInfo(name = "back_color")
    private int backgroundColor;
    @ColumnInfo(name = "date_color")
    private int textColor;

    public List<CalendarBean> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<CalendarBean> calendars) {
        this.calendars = calendars;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof WidgetBean && id == ((WidgetBean) obj).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
