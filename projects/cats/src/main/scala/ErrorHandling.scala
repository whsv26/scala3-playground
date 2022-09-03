package org.whsv26.playground.cats

import cats.data.EitherT
import cats.syntax.applicative._
import cats.{Applicative, Functor, Monad}

class FooService[F[_]: Applicative]:
  def doSomething(): EitherT[F, FooService.Error, Int] =
    EitherT.right(3.pure[F])

object FooService:
  type Error = Error1.type | Error2
  case object Error1
  case class Error2(kek: String)

class BarService[F[_]: Applicative]:
  def doSomething(): EitherT[F, BarService.Error, Int] =
    EitherT.right(2.pure[F])

object BarService:
  type Error = Error3
  case class Error3(lol: String)

class FooBarService[F[_]: Monad](fooService: FooService[F], barService: BarService[F]):
  def doSomething(): EitherT[F, FooBarService.Error, Int] =
    for {
      a <- fooService.doSomething()
      b <- barService.doSomething()
    } yield a + b

object FooBarService:
  type Error = FooService.Error | BarService.Error

extension[F[_]: Functor, A, B] (v: EitherT[F, A, B])
  def widenLeft[E]: EitherT[F, A | E, B] =
    v.leftMap(err => err: A | E)

given [F[_]: Functor, E1, E2, B]: Conversion[EitherT[F, E1, B], EitherT[F, E1 | E2, B]] =
  _.widenLeft[E2]