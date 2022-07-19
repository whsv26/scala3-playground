import scala.compiletime.*
import scala.deriving.Mirror

inline def identify[T]: String =
  inline erasedValue[T] match
    case _: Int => "this is INTEGER"
    case _: Boolean => "this is BOOLEAN"
    case _: Unit => "this is UNIT"
    case _ => "idk, man"

identify[Int]
identify[Boolean]
identify[Unit]
identify[Long]
identify[Char]

type Res[T] = T match
  case Int => Int
  case Boolean => Boolean
  case String => String

inline def defaultValue[T]: Res[T] =
  inline erasedValue[T] match
    case _: Int => 0
    case _: Boolean => false
    case _: String => ""

defaultValue[Int]
defaultValue[Boolean]
defaultValue[String]
