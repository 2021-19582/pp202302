
package lectureCodes.src.Object$minusOriented$u0020Programming



final class MoreOnAbstractClasses$_ {
def args = MoreOnAbstractClasses_sc.args$
def scriptPath = """lectureCodes/src/Object-Oriented Programming/MoreOnAbstractClasses.sc"""
/*<script>*/
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

// test
def generateTree3(n: Int): MyTree3[Int] = {
  def gen(lo: Int, hi: Int): MyTree3[Int] =
    if (lo > hi) Empty3()
    else{
      val mid = (lo + hi) / 2
      Node3(mid, gen(lo, mid-1), gen(mid+1, hi))
    }

  gen(1, n)
}

sumElementsGen3((x: Int)=> x)(generateTree3(100)) // val res1: Int = 5050

generateTree3(5)
// val res2: MyTree3[Int] = Node3(3,Node3(1,Empty3(),Node3(2,Empty3(),Empty3())),Node3(4,Empty3(),Node3(5,Empty3(),Empty3())))
//  3
// / \
//1   4
// \   \
//  2   5

//
// Iter <: Iterable
//
abstract class Iter4[A] /*extends Iterable4[A]*/{
  def getValue: Option[A]
  def getNext: Iter4[A]
  def iter = this
}
abstract class Iterable4[A] {
  def iter: Iter4[A]
}

def sumElementsGen4[A](f: A=>Int)(l: Iter4[A]): Int = {
  l.getValue match{
    case None => 0
    case Some(a) => f(a) + sumElementsGen4(f)(l.getNext)
  }
}

sealed abstract class MyList4[A] extends Iter4[A]
case class MyNil4[A]() extends MyList4[A]{
  override def getValue: Option[A] = None
  override def getNext: Iter4[A] = this
//  override def iter: Iter4[A] = this // unnecessary since already defined in Iter
}
case class MyCons4[A](val hd: A, val tl: MyList4[A]) extends MyList4[A]{
  override def getValue: Option[A] = Some(hd)
  override def getNext: Iter4[A] = tl
}

val lst4: MyList4[Int] =
  MyCons4(3, MyCons4(4, MyCons4(2, MyNil4())))

sumElementsGen4[Int]((x)=>x)(lst4) // val res3: Int = 9

//
// Note: tail-recursive append
//
sealed abstract class MyList5[A] extends Iter4[A]{
  def append(lst: MyList5[A]): MyList5[A] =
    MyList5.revAppend(MyList5.revAppend(this, MyNil5()), lst)
}
object MyList5{
  def revAppend[A] (lst1: MyList5[A], lst2: MyList5[A]): MyList5[A] =
    lst1 match{
      case MyNil5() => lst2
      case MyCons5(hd, tl) => revAppend(tl, MyCons5(hd, lst2))
    }
}
case class MyNil5[A]() extends MyList5[A] {
  override def getValue: Option[A] = None
  override def getNext: Iter4[A] = this
}
case class MyCons5[A](value: A, tl: MyList5[A]) extends MyList5[A] {
  override def getValue: Option[A] = Some(value)
  override def getNext: Iter4[A] = tl
}
/*</script>*/ /*<generated>*/
/*</generated>*/
}

object MoreOnAbstractClasses_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new MoreOnAbstractClasses$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export MoreOnAbstractClasses_sc.script as MoreOnAbstractClasses

