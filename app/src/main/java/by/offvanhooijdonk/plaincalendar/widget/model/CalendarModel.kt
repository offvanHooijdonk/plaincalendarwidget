package by.offvanhooijdonk.plaincalendar.widget.model

//@Entity(tableName = "calendars")
data class CalendarModel(
    //@PrimaryKey
    val id: Long,
    //@ColumnInfo(name = "display_name")
    val displayName: String,
    //@ColumnInfo(name = "account_name")
    val accountName: String,
    //@ColumnInfo(name = "color")
    val color: Int?,
    //@ColumnInfo(name = "primary_on_account")
    val isPrimaryOnAccount: Boolean = false,
) {
    //@Ignore
    var isSelected = false

    override fun equals(other: Any?): Boolean =
        (other as? CalendarModel)?.id == this.id

    override fun hashCode(): Int =
        id.hashCode()
}
