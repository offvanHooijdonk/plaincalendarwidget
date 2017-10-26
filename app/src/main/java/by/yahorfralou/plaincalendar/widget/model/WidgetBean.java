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
    @ColumnInfo(name = "show_event_color")
    private Boolean showEventColor;
    @ColumnInfo(name = "show_today_date")
    private Boolean showTodayDate;
    @ColumnInfo(name = "show_today_day_of_week")
    private Boolean showTodayDayOfWeek;
    @ColumnInfo(name = "show_date_divide_line")
    private Boolean showDateDivider;
    @ColumnInfo(name = "show_end_date")
    private ShowEndDate showEndDate;
    @ColumnInfo(name = "show_today_leading_zero")
    private Boolean showTodayLeadingZero;
    @ColumnInfo(name = "show_date_text_label")
    private Boolean showDateTextLabel;

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

    public Boolean getShowEventColor() {
        return showEventColor;
    }

    public void setShowEventColor(Boolean showEventColor) {
        this.showEventColor = showEventColor;
    }

    public Boolean getShowTodayDate() {
        return showTodayDate;
    }

    public void setShowTodayDate(Boolean showTodayDate) {
        this.showTodayDate = showTodayDate;
    }

    public Boolean getShowTodayDayOfWeek() {
        return showTodayDayOfWeek;
    }

    public void setShowTodayDayOfWeek(Boolean showTodayDayOfWeek) {
        this.showTodayDayOfWeek = showTodayDayOfWeek;
    }

    public Boolean getShowDateDivider() {
        return showDateDivider;
    }

    public void setShowDateDivider(Boolean showDateDivider) {
        this.showDateDivider = showDateDivider;
    }

    public ShowEndDate getShowEndDate() {
        return showEndDate;
    }

    public void setShowEndDate(ShowEndDate showEndDate) {
        this.showEndDate = showEndDate;
    }

    public Boolean getShowTodayLeadingZero() {
        return showTodayLeadingZero;
    }

    public void setShowTodayLeadingZero(Boolean showTodayLeadingZero) {
        this.showTodayLeadingZero = showTodayLeadingZero;
    }

    public Boolean getShowDateTextLabel() {
        return showDateTextLabel;
    }

    public void setShowDateTextLabel(Boolean showDateTextLabel) {
        this.showDateTextLabel = showDateTextLabel;
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
                case 0:
                    value = NO_CORNER;
                    break;
                case 1:
                    value = SMALL;
                    break;
                case 2:
                    value = MEDIUM;
                    break;
                case 3:
                    value = LARGE;
                    break;
                case 4:
                    value = XLARGE;
                    break;
                default:
                    value = getDefault();
            }

            return value;
        }

        public static Corners getDefault() {
            return SMALL;
        }
    }

    public enum ShowEndDate {
        NEVER(0), MORE_THAN_DAY(1), ALWAYS(2);
        private int code;

        ShowEndDate(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public static ShowEndDate fromCode(int code) {
            ShowEndDate value = getDefault();
            for (ShowEndDate val : values()) {
                if (val.getCode() == code) {
                    value = val;
                    break;
                }
            }
            return value;
        }

        public static ShowEndDate getDefault() {
            return NEVER;
        }

    }
}
