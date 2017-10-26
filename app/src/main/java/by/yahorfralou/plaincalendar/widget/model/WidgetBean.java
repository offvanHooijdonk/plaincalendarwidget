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
    @ColumnInfo(name = "days")
    private int days;
    @ColumnInfo(name = "back_color")
    private Integer backgroundColor;
    @ColumnInfo(name = "date_color")
    private Integer textColor;
    @ColumnInfo(name = "opacity")
    private Integer opacity;
    @ColumnInfo(name = "corners_radius")
    private Corners corners;
    @ColumnInfo(name = "text_size_delta")
    private Integer textSizeDelta;

    public List<CalendarBean> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<CalendarBean> calendars) {
        this.calendars = calendars;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
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

    public Corners getCorners() {
        return corners;
    }

    public void setCorners(Corners corners) {
        this.corners = corners;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof WidgetBean && id == ((WidgetBean) obj).id;
    }

    public Integer getTextSizeDelta() {
        return textSizeDelta;
    }

    public void setTextSizeDelta(Integer textSizeDelta) {
        this.textSizeDelta = textSizeDelta;
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

    public enum Corners {
        NO_CORNER(0), SMALL(1), MEDIUM(2), LARGE(3), XLARGE(4);
        private int code;

        Corners(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static Corners fromInt(int code) {
            Corners value;
            switch (code) {
                case 0: value = NO_CORNER; break;
                case 1: value = SMALL; break;
                case 2: value = MEDIUM; break;
                case 3: value = LARGE; break;
                case 4: value = XLARGE; break;
                default: value = getDefault();
            }

            return value;
        }

        public static Corners getDefault() {
            return SMALL;
        }
    }
}
