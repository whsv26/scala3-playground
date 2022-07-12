package org.whsv26.playground.cats

import cats.effect.{Async, Concurrent, ExitCode, IO, IOApp, Temporal}
import cats.implicits.*
import java.time.LocalDate
import scala.util.Random.nextInt
import scala.util.Random.nextBoolean
import scala.util.Random.nextDouble
import scala.concurrent.duration.*
import cats.effect.unsafe.implicits.global

object Asynchronous extends IOApp.Simple {
  object AsyncClient {
    def currentRate(
      currency: String,
      onSuccess: BigDecimal => Unit,
      onError: Throwable => Unit
    ): Unit = {
      val delayed = IO.sleep(nextInt(3).seconds) *> IO {
        if (nextBoolean) {
          onSuccess(BigDecimal(nextDouble))
        } else {
          onError(new RuntimeException(s"Unable to get current rate for $currency"))
        }
      }

      delayed.unsafeRunAndForget()
    }
  }

  def currentRate[F[_]](currency: String)(implicit
    A: Async[F],
    T: Temporal[F]
  ): F[BigDecimal] = {

    A.async[BigDecimal] { cb =>
      AsyncClient.currentRate(
        currency,
        rate => cb(rate.asRight[Throwable]),
        err => cb(err.asLeft[BigDecimal])
      )

      None.pure[F]
    }
  }

  def run: IO[Unit] = {
    val currency = "EUR"
    for {
      rate <- currentRate[IO](currency)
      _ <- IO.println(s"Rate for $currency is $rate")
    } yield ExitCode.Success
  }
}
