package org.whsv26.playground.stdlib

import java.util.concurrent.ConcurrentHashMap
import scala.collection.concurrent.TrieMap
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object ConcurrentCollection extends App {
  def testConcurrentHashMap(): Unit = {
    val map = new ConcurrentHashMap[String, Int]()

    val computation = Future.traverse(1 to 1000) { _ =>
      Future {
        val cur = map.getOrDefault("a", 0)
        map.put("a", cur + 1)
      }
    }

    computation.onComplete { _ =>
      println(map.get("a"))
      assert(map.get("a") != 1000)
    }
  }

  def testTrieMap(): Unit = {
    val map = TrieMap[String, Int]()

    val computation = Future.traverse(1 to 1000) { _ =>
      Future {
        val cur = map.getOrElse("a", 0)
        map.update("a", cur + 1)
      }
    }

    computation.onComplete { _ =>
      println(map("a"))
      assert(map("a") != 1000)
    }
  }

  testConcurrentHashMap()
  testTrieMap()
}
