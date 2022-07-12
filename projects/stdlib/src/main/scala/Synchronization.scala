package org.whsv26.playground.stdlib

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Synchronization extends App {
  class Counter {
    private val count = AtomicInteger(0)
    def toInt: Int = count.get()

    def increment(): Unit = {
      count.incrementAndGet()
    }

    def decrement(): Unit = {
      count.decrementAndGet()
    }
  }

  val ctr = new Counter()

  Future.traverse(1 to 1000) { _ =>
    for {
      _ <- Future(ctr.increment())
      _ <- Future(ctr.decrement())
    } yield ()
  }

  assert(ctr.toInt == 0)
}
