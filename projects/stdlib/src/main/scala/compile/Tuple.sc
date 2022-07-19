import scala.compiletime.*
import scala.deriving.Mirror

trait Show[T]:
  def show(t: T): String

given Show[Int] with
  def show(t: Int): String = s"INT($t)"

given Show[String] with
  def show(t: String): String = s"STRING($t)"

type Pair = (Int, String)
type Tail = Tuple.Tail[Pair]
type MappedToInstances = Tuple.Map[Pair, Show]

val d1: Tuple.Head[Pair] = 1
val d2: Tuple.Head[Tuple.Tail[Pair]] = "a"
val d3: Tuple.Tail[Pair] = "a" *: EmptyTuple

val pair: Pair = 1 -> "a"
val instances: (Show[Int], Show[String]) = summonAll[MappedToInstances]

val p0: Int = pair(0)
val p1: String = pair(1)

val zipMap: Option[(Int, Show[Int])] *: Option[(String, Show[String])] *: EmptyTuple =
  pair
    .zip(instances)
    .map[[t] =>> Option[t]]([T] => (v: T) => Some(v))