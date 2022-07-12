package org.whsv26.playground.stdlib
package visibility.a.aa

import visibility.a.Foo

object Bar {
  Foo.vPrivateA
  Foo.vProtectedA
  Foo.vPublic
}
