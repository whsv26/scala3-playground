package org.whsv26.playground.cats

import cats.effect.kernel.Outcome
import cats.effect.{Fiber, FiberIO, IO, IOApp}
import cats.implicits.*
import scala.concurrent.duration.*
import scala.util.Random

object IntroductionToFibers extends IOApp.Simple {
  val meaningOfLife: IO[Int] = IO(42)
  val favLang: IO[String] = IO("Scala")

  def createFiber: Fiber[IO, Throwable, String] = ???

  extension [A] (fa: IO[A])
    def debug: IO[A] =
      fa.map { a =>
        println(s"[${Thread.currentThread().getName}] $a")
        a
      }

  def sameThread: IO[Unit] = for {
    _ <- meaningOfLife.debug
    _ <- favLang.debug
  } yield ()

  val aFiber: IO[FiberIO[Int]] = meaningOfLife.debug.start

  def differentThreads: IO[Unit] = for {
    _ <- aFiber.debug
    _ <- favLang.debug
  } yield ()

  def runOnAnotherThread[A](io: IO[A]): IO[Outcome[IO, Throwable, A]] =
    for {
      fib <- io.start
      outcome <- fib.join
    } yield outcome

  def throwOnAnotherThread: IO[Outcome[IO, Throwable, Int]] =
    for {
      fib <- IO.raiseError[Int](new RuntimeException("no no no")).start
      outcome <- fib.join
    } yield outcome

  def testCancel: IO[Outcome[IO, Throwable, String]] = {
    val task = IO("starting").debug *> IO.sleep(1.second) *> IO("done").debug

    for {
      fib <- task.start
      _ <- IO.sleep(500.millis) *> IO("canceling").debug
      _ <- fib.cancel
      res <- fib.join
    } yield res
  }

  def run: IO[Unit] = {
    testCancel.debug.void
  }
}
