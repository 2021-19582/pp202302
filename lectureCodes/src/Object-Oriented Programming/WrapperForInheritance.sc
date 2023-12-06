//
// Using Wrapper Class
//
abstract class Iter[A]{
  def getValue: Option[A]
  def getNext: Iter[A]
}
class ListIter[A](val list: List[A]) extends Iter[A]{ // ListIter is a wrapper of List
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


//In Scala, a wrapper class is a class that wraps around a value of a primitive data type or another class.
//It is used to provide additional functionality to the wrapped value, such as adding new methods or modifying existing ones.
//
//Scala provides rich wrappers as a way to enrich some other class and wrap it with additional features.
//Rich wrappers are implicit classes that add new methods to an existing class 2. They are called rich because they enrich the original class with new functionality 2.
//
//For example, suppose we want to enrich the Int class with a method that counts the number of digits in the integer.
//We can define a new implicit class RichInt that extends Int and adds the new method.
//Here’s an example:
//
//  Scala
//
//  implicit class RichInt(val i: Int) extends AnyVal {
//    def countDigits: Int = i.toString.length
//  }
//Now, we can use the countDigits method on any integer value as shown below:
//
//  Scala
//
//  val num = 12345
//  println(num.countDigits) // prints 5
//In this example, RichInt is a wrapper class that wraps around the Int value and adds the countDigits method to it 2.