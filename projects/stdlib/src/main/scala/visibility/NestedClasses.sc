class o1 {
  val v1 = 1

  val v3Exported = new o2().v3Exported

  class o2 {
    val v2 = 2

    val v3Exported = new o3().v3

    private class o3 {
      val v3 = 3
    }
  }
}

val iO1: o1 = new o1()
val iO1O2: iO1.o2 = new iO1.o2
// val iO1O2O3 = new iO1O2.o3 err

// def iO1Def: o1 = new o1()
// new iO1Def.o2 err(not immutable path)


