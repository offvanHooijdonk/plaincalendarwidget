package by.offvanhooijdonk.plaincalendar.widget.model

import java.util.Date

data class EventModel(
    val id: Long = 0,
    val title: String,
    val dateStart: Date,
    val dateEnd: Date,
    val isAllDay: Boolean = false,
    val eventColor: Int? = null,
    val calendarId: Long = 0,
    val eventId: Long = 0,
) {
    override fun equals(other: Any?): Boolean = (other as? EventModel)?.id == id

    override fun hashCode(): Int = id.hashCode()
}
