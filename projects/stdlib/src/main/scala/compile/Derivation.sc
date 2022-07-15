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
  end derived

  inline def showProduct[T](m: Mirror.ProductOf[T]) =
    val labels = summonLabels[m.MirroredElemLabels]
    val instances = summonInstances[m.MirroredElemTypes].map(_.asInstanceOf[Show[Any]])
    new Show[T]:
      def show(t: T): String =
        labels
          .lazyZip(t.asInstanceOf[Product].productIterator.to(Iterable))
          .lazyZip(instances)
          .map((label, elem, instance) => s"$label: ${instance.show(elem)}")
          .mkString(s"${constValue[m.MirroredLabel]}(", ", ", ")")
      end show
  end showProduct

  inline def summonLabels[T <: Tuple]: List[String] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => constValue[t].asInstanceOf[String] :: summonLabels[ts]
  end summonLabels

  inline def summonInstances[T <: Tuple]: List[Show[_]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[Show[t]] :: summonInstances[ts]
  end summonInstances
end Show

given Show[Int] with
  def show(t: Int): String = s"INT($t)"

given Show[Boolean] with
  def show(t: Boolean): String = s"BOOL($t)"

case class Dummy1(a: Int, b: Boolean, c: Int) derives Show

def show[T](t: T)(using S: Show[T]) = S.show(t)

show(Dummy1(1, false, 2))
