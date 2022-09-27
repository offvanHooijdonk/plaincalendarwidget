package by.offvanhooijdonk.plaincalendarv2.widget.model

data class CalendarModel(
    val id: Long,
    val displayName: String,
    val accountName: String,
    val color: Int?,
    val isPrimaryOnAccount: Boolean = false,
) {
    override fun equals(other: Any?): Boolean =
        (other as? CalendarModel)?.id == this.id

    override fun hashCode(): Int =
        id.hashCode()
}
