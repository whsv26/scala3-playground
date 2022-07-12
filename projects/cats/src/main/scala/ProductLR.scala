package org.whsv26.playground.cats

import cats.effect.{IO, IOApp}
import cats.implicits.*
import scala.util.Random
import scala.util.Random.nextInt

object ProductLR extends IOApp.Simple {
  def sameEvaluationOrder = {
    val case1 = IO(println("left")) *> IO(println("right"))
    val case2 = IO(println("left")) <* IO(println("right"))

    case1 *> case2
  }

  def differentValues = {
    val case1 = IO("left") *> IO("right")
    val case2 = IO("left") <* IO("right")

    for {
      c1 <- case1
      c2 <- case2
    } yield (c1, c2)
  }

  def run: IO[Unit] = {
    differentValues.flatMap(IO.println)
  }
}
