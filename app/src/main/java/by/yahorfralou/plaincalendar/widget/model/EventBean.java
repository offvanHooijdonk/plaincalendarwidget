package by.yahorfralou.plaincalendar.widget.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "events")
public class EventBean {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "date_start")
    private Date dateStart;
    @ColumnInfo(name = "date_end")
    private Date dateEnd;
    @ColumnInfo(name = "is_all_day")
    private boolean allDay;
    @ColumnInfo(name = "event_color")
    private int eventColor;
    @ColumnInfo(name = "calendar_id")
    private long calendarId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public int getEventColor() {
        return eventColor;
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof EventBean && id == ((EventBean) obj).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", allDay=" + allDay +
                ", eventColor=" + eventColor +
                ", calendarId=" + calendarId +
                '}';
    }
}
