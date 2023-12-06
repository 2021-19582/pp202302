//
// Abstract Class with Arguments
//
abstract class Iterable[A]{
  type iter_t
  def iter: iter_t
  def getValue(i: iter_t): Option[A]
  def getNext(i: iter_t): iter_t
}

def sumElements(intToInt: Int => Int)(lst: Iterable[Int]): Int = {
  def sumElementsIter(lstIter: lst.iter_t): Int =
    lst.getValue(lstIter) match{
      case Some(v) => intToInt(v) + sumElementsIter(lst.getNext(lstIter))
      case _ => 0
    }
  sumElementsIter(lst.iter)
}

abstract class IterableHE[A](eq: (A, A) => Boolean) extends Iterable[A] {
  def hasElement(a: A): Boolean = {
    def hasElementIter(i: iter_t): Boolean =
      getValue(i) match {
        case Some(n) =>
          if (eq(a, n)) true
          else hasElementIter(getNext(i))
        case _ => false
      }
    hasElementIter(iter)
  }
}

sealed abstract class MyTree[A](eq: (A, A)=>Boolean) extends IterableHE[A](eq) {
  type iter_t = List[A]
  override def getValue(i: List[A]): Option[A] = i.headOption
  override def getNext(i: List[A]): List[A] = i.tail
}
case class Empty[A](eq: (A, A)=>Boolean) extends MyTree[A](eq) {
  val iter: List[A] = Nil
}
case class Node[A](eq: (A, A)=>Boolean, v: A, lt: MyTree[A], rt: MyTree[A]) extends MyTree[A](eq) {
  val iter: List[A] = v::(lt.iter++rt.iter)
}

val Ieq = (x: Int, y: Int) => x == y // a function that takes two Int(s) and returns boolean
val IEmpty = Empty(Ieq)
def INode(n: Int, t1: MyTree[Int], t2: MyTree[Int]) =
  Node(Ieq, n, t1, t2)

val t: MyTree[Int] =
  INode(3,  INode(4,  INode(2, IEmpty, IEmpty),
                      INode(3, IEmpty, IEmpty)),
            INode(5, IEmpty, IEmpty))

sumElements((x: Int)=>x)(t) // val res0: Int = 17
t.hasElement(5) // val res1: Boolean = true
t.hasElement(10) // val res2: Boolean = false

//
// Alternatively, Argument Elimination
//
abstract class IterableHE2[A] extends Iterable[A]
{
  def eq(a: A, b: A): Boolean
  def hasElement(a: A): Boolean =
    def hasElementIter(i: iter_t): Boolean =
      getValue(i) match{
        case Some(n) =>
          if (eq(a, n)) true
          else hasElementIter(getNext(i))
        case _ => false
      }
    hasElementIter(iter)
}

// MyTree
sealed abstract class MyTree2[A] extends IterableHE2[A] {
  type iter_t = List[A]
  override def getValue(i: List[A]): Option[A] = i.headOption
  override def getNext(i: List[A]): List[A] = i.tail
}
case class Empty2[A](_eq:(A,A)=>Boolean) extends MyTree2[A] {
  def eq(a: A, b: A) = _eq(a, b)
  val iter: List[A] = Nil
}
case class Node2[A](_eq:(A,A)=>Boolean, v: A, lt: MyTree2[A], rt: MyTree2[A]) extends MyTree2[A]{
  def eq(a: A, b: A) = _eq(a, b)
  val iter: List[A] = v::(lt.iter ++ rt.iter)
}

val Ieq2 = (x: Int, y: Int) => x==y
val IEmpty2 = Empty2(Ieq2)
def INode2(n: Int, t1: MyTree2[Int], t2: MyTree2[Int]) =
  Node2(Ieq, n, t1, t2)

val t2: MyTree2[Int] =
  INode2(3, INode2(4, INode2(2, IEmpty2, IEmpty2),
    INode2(3, IEmpty2, IEmpty2)),
    INode2(5, IEmpty2, IEmpty2))

sumElements((x: Int) => x)(t)
t.hasElement(5)
t.hasElement(10)