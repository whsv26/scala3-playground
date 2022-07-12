package org.whsv26.playground.fs2

import cats.effect.kernel.Async
import cats.effect.kernel.Sync
import cats.effect.{IO, IOApp}
import fs2.{Chunk, Pure, Stream}
import scala.concurrent.duration.*

object FS2 extends IOApp.Simple {
  def joinNestedStreams = {
    val s1 = Stream
      .repeatEval(IO.println("Fiber 1"))
      .metered(300.millis)
      .take(7)

    val s2 = Stream
      .repeatEval(IO.println("Fiber 2"))
      .metered(500.millis)
      .take(10)

    Stream(s1, s2).parJoinUnbounded
  }

  def parEvalMapUnordered = {
    def writeToSocket[F[_] : Sync](chunk: Chunk[String]): F[Unit] =
      Sync[F].delay {
        println(s"[thread: ${Thread.currentThread().getName}] :: Writing $chunk to socket")
      }

    Stream.emits(1 to 100)
      .map(_.toString)
      .chunkN(10)
      .covary[IO]
      .parEvalMapUnordered(10)(writeToSocket[IO])
  }

  def run: IO[Unit] = {
    parEvalMapUnordered.compile.drain
  }
}
