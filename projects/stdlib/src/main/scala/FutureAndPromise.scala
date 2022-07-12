package org.whsv26.playground.stdlib

import java.util.concurrent.{Executors, ScheduledFuture, TimeUnit}
import scala.concurrent.{Await, Future, Promise}
import scala.io.Source
import scala.util.Try
import scala.concurrent.duration._

object FutureAndPromise extends App {

  def case0(): Unit = {
    val p = Promise[Int]
    p.complete(Try(1))
    p.future
  }

  def case1(): Unit = {
    val buff = Source.fromResource("in.txt")
    println(buff.getLines().toList)
    buff.close()
  }

  def case2(): Unit = {
    val somePromise = Promise[String]
    val scheduler = Executors.newSingleThreadScheduledExecutor()
    val duration = 100.millis

    val javaFuture: ScheduledFuture[String] = scheduler.schedule(
      () => {
        somePromise.success("Promise fulfilled")
        "Java future executed"
      },
      duration.length,
      duration.unit
    )

    scheduler.shutdown()

    val res = Await.result(somePromise.future, 5.seconds)
    println(res)
  }

  case2()
}
