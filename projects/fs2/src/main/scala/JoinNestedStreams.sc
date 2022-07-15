import cats.effect.kernel.Async
import cats.effect.kernel.Sync
import cats.effect.{IO, IOApp}
import cats.effect.unsafe.implicits.global
import fs2.{Chunk, Pure, Stream}
import scala.concurrent.duration.*

val s1 = Stream
  .repeatEval(IO.println("Fiber 1"))
  .metered(300.millis)
  .take(7)

val s2 = Stream
  .repeatEval(IO.println("Fiber 2"))
  .metered(500.millis)
  .take(10)

Stream(s1, s2)
  .parJoinUnbounded
  .compile
  .drain
  .unsafeRunSync()