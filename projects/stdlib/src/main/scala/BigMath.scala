package org.whsv26.playground.stdlib

import scala.util.Random

object BigMath extends App {

  def nextBigInt() = BigInt {
    LazyList.from(1 to 100)
      .map(_ => Random.nextInt(10).toString)
      .mkString
  }

  def nextBigDecimal() = BigDecimal {
    LazyList.from(1 to 100)
      .map(_ => Random.nextInt(10).toString)
      .appended(".")
      .appendedAll {
        LazyList.range(0, 10)
          .map(_ => Random.nextInt(10).toString)
      }
      .mkString
  }

  val bigInt1 = nextBigInt()
  val bigInt2 = nextBigInt()

  println(bigInt1)
  println(bigInt2)
  println(bigInt1 + bigInt2)

  val bigDecimal1 = nextBigDecimal()
  val bigDecimal2 = nextBigDecimal()

  println(bigDecimal1)
  println(bigDecimal2)
  println(bigDecimal1 + bigDecimal2)
}
