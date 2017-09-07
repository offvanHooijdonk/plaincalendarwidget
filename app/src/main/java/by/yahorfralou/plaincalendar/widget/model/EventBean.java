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
    @ColumnInfo(name = "date")
    private Date date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
