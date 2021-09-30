package by.offvanhooijdonk.plaincalendar.widget.view.configure

import by.offvanhooijdonk.plaincalendar.widget.model.CalendarModel
import by.offvanhooijdonk.plaincalendar.widget.model.WidgetModel

interface IConfigureView {
    fun displayCalendarsDialog(calendars: List<CalendarModel>)
    fun onWidgetSettingsLoaded(widget: WidgetModel)
    fun showCalendarsLoadProgress(isShow: Boolean)
    fun showNoWidgets(isShow: Boolean)

    fun notifyChangesAndFinish()
}
