package org.whsv26.playground.zio

import zio.clock.Clock
import zio.console.Console
import zio.{Exit, ExitCode, UIO, URIO, ZIO}
import zio.duration.*

object ScalaWithZIOIntroductionToFibers extends zio.App {
  // ZIO[R, E, A]
  // type UIO[+A] = ZIO[Any, Nothing, A] // universal IO

  val zmol: UIO[Int] = ZIO.succeed(42)

  // concurrency = daily routine of Bob
  val showerTime = ZIO.succeed("Taking a shower")
  val boilingWater = ZIO.succeed("Boiling some water")
  val preparingCoffee = ZIO.succeed("Preparing some coffee")

  def printThread = s"${Thread.currentThread().getName}"

  def synchronousRoutine: UIO[Unit] = for {
    _ <- showerTime.debug(printThread)
    _ <- boilingWater.debug(printThread)
    _ <- preparingCoffee.debug(printThread)
  } yield ()

  // fiber = schedulable computation
  // Fiber[E, A]

  def concurrentShowerWhileBoilingWaterRoutine: UIO[Unit] = for {
    _ <- showerTime.debug(printThread).fork
    _ <- boilingWater.debug(printThread)
    _ <- preparingCoffee.debug(printThread)
  } yield ()

  def concurrentRoutine = for {
    showerFiber <- showerTime.debug(printThread).fork
    boilingWaterFiber <- boilingWater.debug(printThread).fork
    zippedFiber = showerFiber.zip(boilingWaterFiber)
    result <- zippedFiber.join.debug(printThread)
    _ <- ZIO.succeed(s"$result done").debug(printThread) *> preparingCoffee.debug(printThread)
  } yield ()

  val callFromAlice = ZIO.succeed("Call from Alice")
  val boilingWaterWithTime = boilingWater.debug(printThread) *>
    ZIO.sleep(5.second) *>
    ZIO.succeed("Boiled water ready")

  def concurrentRoutineWithAliceCall = for {
    _ <- showerTime.debug(printThread)
    boilingFiber <- boilingWaterWithTime.debug(printThread).fork

    _ <- callFromAlice.debug(printThread).fork
      *> ZIO.sleep(2.seconds)
      *> boilingFiber.interrupt.debug(printThread)

    _ <- ZIO.succeed("Screw my coffee, going with Alice").debug(printThread)
  } yield ()

  val prepareCoffeeWithTime = preparingCoffee.debug(printThread)
    *> ZIO.sleep(5.seconds)
    *> ZIO.succeed("Coffee ready")

  def concurrentRoutineWithCoffeeAtHome = for {
    _ <- showerTime.debug(printThread)
    _ <- boilingWater.debug(printThread)
    coffeeFiber <- prepareCoffeeWithTime.debug(printThread).fork.uninterruptible
    result <- callFromAlice.debug(printThread).fork *> coffeeFiber.interrupt.debug(printThread)
    _ <- result match {
      case Exit.Success(value) => ZIO.succeed("Sorry Alice, making breakfast at home").debug(printThread)
      case _ => ZIO.succeed("Going to a cafe with Alice").debug(printThread)
    }
  } yield ()

  def run(args: List[String]): URIO[Console with Clock, ExitCode] = {
    concurrentRoutineWithCoffeeAtHome.exitCode
  }
}
