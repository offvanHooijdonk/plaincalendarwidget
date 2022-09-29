package by.offvanhooijdonk.plaincalendarv2.widget.ext

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*

var MILLIS_IN_DAY = 24 * 60 * 60 * 1000L

val LocalDateTime.closestMidnightMillis: Long
    get() = toMidnightAtDay(1).millis

fun LocalDateTime.toMidnightAtDay(days: Long): LocalDateTime = plusDays(days).truncatedTo(ChronoUnit.DAYS)

val LocalDateTime.millis: Long get() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

val LocalDateTime.isToday: Boolean
    get() = truncatedTo(ChronoUnit.DAYS).isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))
val LocalDateTime.isTomorrow: Boolean
    get() = truncatedTo(ChronoUnit.DAYS).isEqual(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS))

val LocalDate.isToday: Boolean
    get() = isEqual(LocalDate.now())
val LocalDate.isTomorrow: Boolean
    get() = isEqual(LocalDate.now().plusDays(1))
