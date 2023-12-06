//
// Using an Associate Type
//
abstract class Iterable[A] {
  type iter_t // an asssociate type of Iterable[A]
  def iter: iter_t
  def getValue(i: iter_t): Option[A]
  def getNext(i: iter_t): iter_t
}

def sumElements[A](f: A=>Int)(xs: Iterable[A]): Int ={
  def sumElementsIter(i: xs.iter_t): Int =
    xs.getValue(i) match{
      case Some(value) => f(value) + sumElementsIter(xs.getNext(i))
      case _ => 0
    }

  sumElementsIter(xs.iter)
}

// MyTree using list
sealed abstract class MyTree[A] extends Iterable[A] {
  override type iter_t = List[A]
  override def getValue(i: List[A]): Option[A] = i.headOption
  override def getNext(i: List[A]): List[A] = i.tail
}
case class Empty[A]() extends MyTree[A] {
  val iter: List[A] = Nil
}
case class Node[A] (v: A, lt: MyTree[A], rt: MyTree[A]) extends MyTree[A] {
  val iter: List[A] = v :: (lt.iter ++ rt.iter) // Pre-order
//  val iter: List[A] = lt.iter ++ (v :: rt.iter) // In-order
//  val iter: List[A] = lt.iter ++ (rt.iter ++ List(v)) // Post-order
}

val t: MyTree[Int] =
  Node(3, Node(4, Node(2, Empty(), Empty()),
    Node(3, Empty(), Empty())),
    Node(5, Empty(), Empty()))

sumElements((x: Int) => x)(t) // val res0: Int = 17