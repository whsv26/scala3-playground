import java.time.temporal.ChronoUnit
import java.time.{Clock, Instant, LocalDate, LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}

val zoneId = ZoneId.of("Europe/Moscow")
val systemClock = Clock.system(zoneId)
val fixedClock = Clock.fixed(
  Instant.now().minus(5, ChronoUnit.DAYS),
  zoneId
)

LocalDate.now(fixedClock)
LocalDateTime.now(fixedClock)
ZonedDateTime.now(fixedClock)

LocalDate.now(systemClock)
LocalDateTime.now(systemClock)
ZonedDateTime.now(systemClock)

LocalDate.ofInstant(Instant.now(), zoneId)
LocalDateTime.ofInstant(Instant.now(), zoneId)
ZonedDateTime.ofInstant(Instant.now(), zoneId)