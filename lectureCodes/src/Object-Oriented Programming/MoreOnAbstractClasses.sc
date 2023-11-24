//
// Problem: Iter for MyTree
//
abstract class Iter[A] {
  def getValue: Option[A]
  def getNext: Iter[A]
}

// MyTree
sealed abstract class MyTree[A]
case class Empty[A]() extends MyTree[A]
case class Node[A] (val value: A, val left: MyTree[A], val right: MyTree[A]) extends MyTree[A]

// MyTree[A] implements Iter[A]
sealed abstract class MyTree2[A] extends Iter[A]
case class Empty2[A]() extends MyTree2[A]{
  override def getValue: Option[A] = None
  override def getNext: Iter[A] =
//    throw new Exception("...")
    this // returns self
}
case class Node2[A](val v: A, val lt: MyTree2[A], val rt: MyTree2[A]) extends MyTree2[A]{
  override def getValue: Option[A] = Some(v)
  override def getNext: MyTree2[A] = new
}