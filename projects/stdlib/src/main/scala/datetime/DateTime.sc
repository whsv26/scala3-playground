import java.time.{Duration, Instant, LocalDate, LocalDateTime, LocalTime, Month, Period, ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.time.temporal.{ChronoUnit, TemporalUnit}

val localDate = LocalDate.of(2020, Month.MARCH, 8)
val localTime = LocalTime.of(12, 30)
val localDateTime = LocalDateTime.of(localDate, localTime)

val zoneRegion = ZoneId.of("Europe/Moscow")
val zoneOffset = ZoneId.of("+03:00")

assert(zoneOffset == zoneRegion.getRules.getOffset(localDateTime))

val zonedDateTime = ZonedDateTime.of(localDateTime, zoneRegion)

zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

val instant = zonedDateTime.toInstant

Duration.of(10, ChronoUnit.MINUTES)
Duration.between(localDateTime.minusSeconds(10), localDateTime)

Period.ofDays(2)
Period.between(localDate.minusDays(44), localDate)