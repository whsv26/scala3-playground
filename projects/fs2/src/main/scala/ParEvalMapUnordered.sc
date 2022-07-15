import cats.effect.kernel.Async
import cats.effect.kernel.Sync
import cats.effect.{IO, IOApp}
import cats.effect.unsafe.implicits.global
import fs2.{Chunk, Pure, Stream}
import scala.concurrent.duration.*

def writeToSocket[F[_] : Sync](chunk: Chunk[String]): F[Unit] =
  Sync[F].delay {
    println(s"[thread: ${Thread.currentThread().getName}] :: Writing $chunk to socket")
  }

Stream.emits(1 to 100)
  .map(_.toString)
  .chunkN(10)
  .covary[IO]
  .parEvalMapUnordered(10)(writeToSocket[IO])
  .compile
  .drain
  .unsafeRunSync()