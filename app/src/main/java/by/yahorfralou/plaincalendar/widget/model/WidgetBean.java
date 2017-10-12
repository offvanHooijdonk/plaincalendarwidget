package by.yahorfralou.plaincalendar.widget.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;
import java.util.Objects;

@Entity(tableName = "widgets")
public class WidgetBean {
    @PrimaryKey
    private long id;
    @Ignore
    private List<CalendarBean> calendars;
    @ColumnInfo(name = "back_color")
    private Integer backgroundColor;
    @ColumnInfo(name = "date_color")
    private Integer textColor;
    @ColumnInfo(name = "opacity")
    private Integer opacity;

    public List<CalendarBean> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<CalendarBean> calendars) {
        this.calendars = calendars;
    }

    public Integer getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Integer backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getTextColor() {
        return textColor;
    }

    public void setTextColor(Integer textColor) {
        this.textColor = textColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getOpacity() {
        return opacity;
    }

    public void setOpacity(Integer opacity) {
        this.opacity = opacity;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof WidgetBean && id == ((WidgetBean) obj).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{"
                + "id=" + id + "\n"
                + ",backgroundColor=" + backgroundColor + "\n"
                + ",dateColor=" + textColor + "\n"
                + "}";
    }
}
