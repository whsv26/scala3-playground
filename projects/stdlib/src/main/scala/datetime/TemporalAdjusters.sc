import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoUnit, TemporalAdjusters, TemporalUnit}
import java.time.*

val localDate = LocalDate.of(2020, Month.MARCH, 8)
val localTime = LocalTime.of(12, 30)
val localDateTime = LocalDateTime.of(localDate, localTime)

val zoneRegion = ZoneId.of("Europe/Moscow")
val zonedDateTime = ZonedDateTime.of(localDateTime, zoneRegion)

zonedDateTime
  .`with`(TemporalAdjusters.firstDayOfYear())
  .withHour(11)

Month.values.map { month =>
  LocalDate.now()
    .withMonth(month.getValue)
    .`with`(TemporalAdjusters.lastDayOfMonth())
    .getDayOfWeek
}.toList

