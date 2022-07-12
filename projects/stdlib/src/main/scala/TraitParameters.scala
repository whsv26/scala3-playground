package org.whsv26.playground.stdlib

object TraitParameters extends App:

  trait Functor[F[_]]:
    extension [A](fa: F[A])
      def map[B](f: A => B): F[B]

  object Functor:
    given Functor[List] with
      extension [A](fa: List[A])
        def map[B](f: A => B): List[B] = fa.map(f)

    given Functor[Option] with
      extension [A](fa: Option[A])
        def map[B](f: A => B): Option[B] = fa.map(f)

  trait Foo[F[_]: Functor]:
    def doStuff[A](fa: F[A]): F[Int] = fa.map(_ => 0)

  class BarList extends Foo[List]
  class BarOption extends Foo[Option]

  println(new BarList().doStuff(List(1, 2, 3)))
  println(new BarOption().doStuff(Some(999)))
