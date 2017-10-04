package by.yahorfralou.plaincalendar.widget.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "calendars")
public class CalendarBean {
    @PrimaryKey
    private long id;
    @ColumnInfo(name = "display_name")
    private String displayName;
    @ColumnInfo(name = "account_name")
    private String accountName;
    @ColumnInfo(name = "color")
    private Integer color;
    @ColumnInfo(name = "primary_on_account")
    private boolean primaryOnAccount;
    @ColumnInfo(name = "is_selected")
    private boolean selected;
    @ColumnInfo(name = "widget_id")
    private Long widgetId;

    public CalendarBean() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isPrimaryOnAccount() {
        return primaryOnAccount;
    }

    public void setPrimaryOnAccount(boolean primaryOnAccount) {
        this.primaryOnAccount = primaryOnAccount;
    }

    public Long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(Long widgetId) {
        this.widgetId = widgetId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
                obj instanceof CalendarBean &&
                this.id == ((CalendarBean) obj).id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CalendarBean{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", color=" + color +
                ", selected=" + selected +
                ", primary=" + primaryOnAccount +
                ", widgetId=" + widgetId +
                '}';
    }
}
