package org.whsv26.playground.cats

import cats.implicits.*
import cats.effect.{IO, IOApp}
import scala.util.Random
import Random.nextInt

/**
 * Blocking IO thread pool:
 * - For preemptive multitasking
 * - Using Java Threads
 * - Java Thread is using OS Scheduler and is preemptive
 * - Java Thread has a priority
 *
 * Non-blocking IO thread pool:
 * - For cooperative multitasking
 * - Using async fibers
 */
object Blocking extends IOApp.Simple {
  extension [A] (fa: IO[A])
    def debug: IO[A] =
      for {
        a <- fa
        _ <- IO.println(s"[$showThread}] $a")
      } yield a

  def showThread: String =
    Thread.currentThread.getName

  def testBlocking1: IO[String] =
    for {
      s1 <- IO("Step 1").debug
      _ <- IO.blocking {
        println(s"I'm blocked on $showThread")
        Thread.sleep(3000)
        println(s"I'm unblocked on $showThread")
      }
      s2 <- IO("Step 2").debug
    } yield s1 + s2

  def testBlocking2: IO[Unit] =
    IO.println(s"current pool").debug >>
      IO.blocking(println(s"[$showThread] blocking pool")) >>
      IO.println(s"current pool $showThread").debug

  def blockingTask(i: Int): IO[Int] =
    IO.blocking {
      println(s"[$showThread] Task $i start")
      Thread.sleep(nextInt(500) + 100)
      println(s"[$showThread] Task $i end")
      i
    }

  def run: IO[Unit] = {
    List.range(1, 1000)
      .parTraverse(blockingTask)
      .map(_.sum)
      .flatMap(IO.println(_))
  }
}
