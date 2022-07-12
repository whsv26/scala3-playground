package org.whsv26.playground.stdlib.collection

import scala.collection.mutable

// Analog to java ArrayList
val arrayBuilder = mutable.ArrayBuilder.make[Int]

// change internal array capacity to hinted size
arrayBuilder.sizeHint(10)

arrayBuilder.addOne(1)
arrayBuilder.addOne(2)

arrayBuilder.length
arrayBuilder.result.toList