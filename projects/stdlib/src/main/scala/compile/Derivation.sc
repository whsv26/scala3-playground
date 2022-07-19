import scala.compiletime.*
import scala.compiletime.summonAll
import scala.deriving.Mirror

trait Show[T]:
  def show(t: T): String

object Show:
  inline given derived[T](using m: Mirror.Of[T]): Show[T] =
    inline m match
      case s: Mirror.SumOf[T] => ???
      case p: Mirror.ProductOf[T] => showProduct[T](p)

  private inline def showProduct[T](m: Mirror.ProductOf[T]) =
    new Show[T]:
      def show(t: T): String =
        summonLabels[m.MirroredElemLabels]
          .lazyZip(t.asInstanceOf[Product].productIterator.to(Iterable))
          .lazyZip(summonInstances[m.MirroredElemTypes])
          .map((label, elem, instance) => s"$label: ${instance.show(elem)}")
          .mkString(s"${constValue[m.MirroredLabel]}(", ", ", ")")

  private inline def summonLabels[T <: Tuple]: List[String] =
    constValueTuple[T].toList.asInstanceOf[List[String]]

  private inline def summonInstances[T <: Tuple]: List[Show[Any]] =
    summonAll[Tuple.Map[T, Show]].toList.asInstanceOf[List[Show[Any]]]
end Show

given Show[Int] with
  def show(t: Int): String = s"INT($t)"

given Show[Boolean] with
  def show(t: Boolean): String = s"BOOL($t)"

case class Dummy1(a: Int, b: Boolean, c: Int) derives Show

def show[T](t: T)(using S: Show[T]) = S.show(t)

show(Dummy1(1, false, 2))
