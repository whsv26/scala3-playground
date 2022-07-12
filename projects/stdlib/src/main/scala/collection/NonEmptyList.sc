case class NonEmptyList[+A](head: A, tail: List[A]) {
  def map[B](f: A => B): NonEmptyList[B] =
    NonEmptyList(f(head), tail.map(f))

  def filter(f: A => Boolean): List[A] =
    (head :: tail).filter(f)
}

object NonEmptyList {
  def apply[A](tail: List[A]): Option[NonEmptyList[A]] =
    tail match {
      case h :: tail => Some(NonEmptyList(h, tail))
      case _ => None
    }
}

println(1)
