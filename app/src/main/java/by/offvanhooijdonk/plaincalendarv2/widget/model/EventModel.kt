package by.offvanhooijdonk.plaincalendarv2.widget.model

import java.time.LocalDateTime

data class EventModel(
    val id: Long = 0,
    val title: String,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime,
    val isAllDay: Boolean = false,
    val eventColor: Int? = null,
    val calendarId: Long = 0,
    val eventId: Long = 0,
) {
    override fun equals(other: Any?): Boolean = (other as? EventModel)?.id == id

    override fun hashCode(): Int = id.hashCode()
}
