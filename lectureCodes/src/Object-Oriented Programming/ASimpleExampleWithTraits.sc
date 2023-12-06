// Motivation
abstract class Iter[A]{
  def getValue: Option[A]
  def getNext: Iter[A]
}

class ListIter[A](val list: List[A]) extends Iter[A] {
  override def getValue: Option[A] = list.headOption
  override def getNext: Iter[A] = new ListIter(list.tail)
}

abstract class Dict[K, V] {
  def add(k: K, v: V): Dict[K, V]
  def find(k: K): Option[V]
}

// Interface using Traits
trait Dict1[K, V] {
  def add(k: K, v: V): Dict1[K, V]
  def find(k: K): Option[V]
}

class ListIterDict[K, V] (eq: (K, K)=> Boolean, list: List[(K,V)]) extends ListIter[(K, V)](list) with Dict1[K, V]{
  override def add(k: K, v: V): ListIterDict[K, V] = new ListIterDict(eq, (k, v) :: list)

  override def find(k: K): Option[V] = {
    def go(l: List[(K, V)]): Option[V] = l match{
      case (k1, v1) :: tl =>
        if (eq(k, k1)) Some(v1) else go(tl)
      case _ => None
    }
    go(list)
  }
}

// Test
def sumElements[A] (f: A=> Int)(xs: Iter[A]): Int =
  xs.getValue match {
    case Some(n) => f(n) + sumElements(f)(xs.getNext)
    case _ => 0
  }

def find_3(d: Dict1[Int, String]) = d.find(3)

val d0 = new ListIterDict[Int, String]((x, y)=> x==y, Nil)
val d = d0.add(4, "four").add(3, "three")

sumElements[(Int, String)](x=>x._1)(d) // val res0: Int = 7
find_3(d) // val res1: Option[String] = Some(three)