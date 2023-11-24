//
// Problem: Iter for MyTree
//
abstract class Iter[A] {
  def getValue: Option[A]
  def getNext: Iter[A]
}

def sumElements[A](f: A=> Int)(l: Iter[A]): Int=
  l.getValue match {
    case None => 0
    case Some(n) => f(n) + sumElements(f)(l.getNext)
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
  override def getNext: MyTree2[A] =
    println(v) // to keep track of things ...
    lt match{
      case Empty2() => {
        rt
      }
      case Node2(lv, llt, lrt) => {
        def merge_rTree(lrTree: MyTree2[A]): MyTree2[A]=
          lrTree match{
            case Empty2() => rt
            case Node2(lrv, lrlt, lrrt) => new Node2(lrv, lrlt, merge_rTree(lrrt))
          }
        new Node2(lv, llt, merge_rTree(lrt))
      }
    }

//  def merge(remainder: MyTree[A]): MyTree[A] =
//    rt match {
//      case Empty2() => remainder
//      case Node2(rv, rlt, rrt) => rrt.merge(remainder)
//    } // we cannot add hew functions in subclass
}

val t20 = Node2(3, Node2(7, Node2(2, Empty2(), Empty2()), Empty2()), Node2(8, Node2(10, Empty2(), Node2(12, Empty2(), Empty2())), Empty2()))
sumElements[Int]((x)=>x*x)(t20)
//3
//7
//2
//8
//10
//12
//val res0: Int = 370

//
// Solution: Better Interface
//
abstract class Iterable3[A] {
  def iter: Iter[A]
}

def sumElements3[A](f: A => Int)(xs: Iter[A]): Int=
  xs.getValue match{
    case None => 0
    case Some(n) => f(n) + sumElements3(f)(xs.getNext)
  }

def sumElementsGen3[A](f: A => Int)(xs: Iterable3[A]): Int=
  sumElements3(f)(xs.iter)

sealed abstract class MyList3[A] extends Iter[A]{
  def append(lst: MyList3[A]): MyList3[A]
}
case class MyNil3[A]() extends MyList3[A]{
  override def getValue: Option[A] = None
  override def getNext: Iter[A] = throw new Exception("...")
  override def append(lst: MyList3[A]): MyList3[A] = lst
}
case class MyCons3[A](hd: A, tl: MyList3[A]) extends MyList3[A]{
  override def getValue: Option[A] = Some(hd)
  override def getNext: Iter[A] = tl
  override def append(lst: MyList3[A]): MyList3[A] = MyCons3(hd, tl.append(lst))
}

// change to iterable while creation
sealed abstract class MyTree3[A] extends Iterable3[A]{
  def iter: MyList3[A]
}

case class Empty3[A]() extends MyTree3[A]{
  val iter = MyNil3()
}
case class Node3[A](v: A, lt: MyTree3[A], rt: MyTree3[A]) extends MyTree3[A]{
  val iter = MyCons3(v, (lt.iter).append(rt.iter))
}