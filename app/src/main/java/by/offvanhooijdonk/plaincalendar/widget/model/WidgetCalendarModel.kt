package by.offvanhooijdonk.plaincalendar.widget.model

//@Entity(tableName = "widget_calendar", primaryKeys = ["widget_id", "calendar_id"]) // TODO add foreign keys
data class WidgetCalendarModel(
    /*@ColumnInfo(name = "widget_id")*/ val widgetId: Long,
    /*@ColumnInfo(name = "calendar_id") */val calendarId: Long,
)
