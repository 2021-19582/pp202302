//
// Using Wrapper Class
//
abstract class Iter[A]{
  def getValue: Option[A]
  def getNext: Iter[A]
}
class ListIter[A](val list: List[A]) extends Iter[A]{
  def getValue = list.headOption
  override def getNext: ListIter[A] = new ListIter(list.tail)
}

def sumElements(intToInt: Int => Int)(lst: ListIter[Int]): Int = {
  lst.getValue match{
    case Some(value) => intToInt(value)+sumElements(intToInt)(lst.getNext)
    case _ => 0
  }
}

sumElements((x: Int)=>x)(new ListIter(List(1, 2, 3, 4))) // val res0: Int = 10

// MyTree Using ListIter
abstract class Iterable[A] {
  def iter : Iter[A]
}
sealed abstract class MyTree[A] extends Iterable[A] {
  override def iter: ListIter[A]
}
case class Empty[A]() extends MyTree[A] {
  val iter: ListIter[A] = new ListIter(Nil)
}
case class Node[A](v: A, lt: MyTree[A], rt: MyTree[A]) extends MyTree[A] {
  val iter: ListIter[A] = new ListIter(v::(lt.iter.list ++ rt.iter.list))
}

val t: MyTree[Int] =
  Node(3, Node(4, Node(2, Empty(), Empty()),
    Node(3, Empty(), Empty())),
    Node(5, Empty(), Empty()))
def sumElementsGen(f: Int=>Int)(x: MyTree[Int]) = sumElements(f)(x.iter)
sumElementsGen((x: Int)=>x)(t) // val res1: Int = 17