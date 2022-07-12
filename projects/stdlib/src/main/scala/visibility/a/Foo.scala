package org.whsv26.playground.stdlib
package visibility.a

object Foo {
  private val vPrivate = 0
  private[this] val vPrivateThis = 0
  private[Foo] val vPrivateFoo = 0
  private[a] val vPrivateA = 0
  protected val vProtected = 0
  protected[this] val vProtectedThis = 0
  protected[a] val vProtectedA = 0
  val vPublic = 0
}
