import scala.compiletime.*
import scala.compiletime.summonAll
import scala.deriving.Mirror

trait Show[T]:
  def show(t: T): String

object Show:
  inline given derived[T](using m: Mirror.Of[T]): Show[T] =
    inline m match
      case s: Mirror.SumOf[T] => showSum[T](s)
      case p: Mirror.ProductOf[T] => showProduct[T](p)

  private inline def showSum[T](m: Mirror.SumOf[T]) =
    new Show[T]:
      val instances = summonInstances[m.MirroredElemTypes].toArray
      def show(t: T): String = instances(m.ordinal(t)).show(t)

  private inline def showProduct[T](m: Mirror.ProductOf[T]) =
    new Show[T]:
      val labels = summonLabels[m.MirroredElemLabels]
      val instances = summonInstances[m.MirroredElemTypes].toArray
      def elems(t: T) = t.asInstanceOf[Product].productIterator.toList
      def show(t: T): String =
        elems(t)
          .lazyZip(labels)
          .lazyZip(instances)
          .map((elem, label, instance) => s"$label: ${instance.show(elem)}")
          .mkString(s"${constValue[m.MirroredLabel]}(", ", ", ")")

  private inline def summonLabels[T <: Tuple] =
    constValueTuple[T].toList.asInstanceOf[List[String]]

  private inline def summonInstances[T <: Tuple] =
    summonAll[Tuple.Map[T, Show]].toList.asInstanceOf[List[Show[Any]]]
end Show

given Show[Int] with
  def show(t: Int): String = s"INT($t)"

given Show[Boolean] with
  def show(t: Boolean): String = s"BOOL($t)"

sealed trait Dummy
case class Dummy1(a: Int, b: Boolean, c: Int) extends Dummy derives Show
case class Dummy2(x: Boolean, y: Int, z: Boolean) extends Dummy  derives Show

def show[T](t: T)(using S: Show[T]) = S.show(t)

show(Dummy1(1, false, 2))
show(Dummy1(1, false, 2): Dummy)
show(Dummy2(false, 999, true): Dummy)
