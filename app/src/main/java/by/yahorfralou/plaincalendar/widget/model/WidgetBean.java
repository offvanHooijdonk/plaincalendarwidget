package by.yahorfralou.plaincalendar.widget.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "widgets")
public class WidgetBean {
    @PrimaryKey
    private int id;

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
