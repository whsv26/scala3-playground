package org.whsv26.playground.cats

import cats.effect.kernel.Outcome
import cats.effect.{Fiber, FiberIO, IO, IOApp}
import cats.implicits.*
import scala.concurrent.duration.*
import scala.util.Random

object RacingIOs extends IOApp.Simple {
  extension [A] (fa: IO[A])
    def debug: IO[A] =
      fa.map { a =>
        println(s"[${Thread.currentThread().getName}] $a")
        a
      }

  val meaningOfLife: IO[Int] = IO(42)

  val valuableIO: IO[Int] = IO("task: starting").debug *>
    IO.sleep(1.second) *>
    IO("task: ending").debug *>
    IO(42)

  val vIO: IO[Int] = valuableIO.onCancel(IO("task: canceling").debug.void)

  val timeout: IO[Unit] = IO("timeout: starting").debug *>
    IO.sleep(500.millis) *>
    IO("timeout: ending").debug.void

  def testRace: IO[String] = {
    val firstIO: IO[Either[Int, Unit]] = IO.race(vIO, timeout)

    firstIO.flatMap {
      case Left(v) => IO(s"Task won: $v")
      case Right(_) => IO(s"Timeout won")
    }
  }

  /**
   * Race and raise error if timeout elapsed
   */
  def testTimeout: IO[Int] = {
    vIO.timeout(500.millis)
  }

  type IOOutcome[A] = IO[Outcome[IO, Throwable, A]]

  def testRacePair[A](iox: IO[A], ioy: IO[A]): IOOutcome[A] = {
    val pair = IO.racePair(iox, ioy)

    pair.flatMap {
      case Left((ioxOutcome, ioyFiber)) => ioyFiber.cancel
        *> IO("First task won").debug
        *> IO(ioxOutcome)
      case Right((ioxFiber, ioyOutcome)) => ioxFiber.cancel
        *> IO("Second task won").debug
        *> IO(ioyOutcome)
    }
  }

  override def run: IO[Unit] = {
    testRacePair(vIO, timeout).debug.void
  }
}
