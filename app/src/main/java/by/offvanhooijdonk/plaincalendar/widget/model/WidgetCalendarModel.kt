package by.offvanhooijdonk.plaincalendar.widget.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetCalendarModel
import java.util.Objects

@Entity(tableName = "widget_calendar", primaryKeys = ["widget_id", "calendar_id"]) // TODO add foreign keys
data class WidgetCalendarModel(
    @ColumnInfo(name = "widget_id") val widgetId: Long,
    @ColumnInfo(name = "calendar_id") val calendarId: Long,
)
