//
// Abstract Class: Interface
//
abstract class Iter[A] {
  def getValue: Option[A]
  def getNext: Iter[A]
}

def sumElements[A] (f: A=>Int)(xs: Iter[A]): Int =
  xs.getValue match{
    case None=> 0
    case Some(n)=> f(n) + sumElements(f)(xs.getNext)
  }

def sumElementsId(xs: Iter[Int])=
//  sumElements[Int]((x)=>x)(xs) // this works too!
  sumElements((x:Int)=>x)(xs)

//
// Concrete Class: Implementation
//
sealed abstract class MyList1[A] extends Iter[A]
case class MyNil1[A]() extends MyList1[A]{
  override def getValue: Option[A] = None
  override def getNext = throw new Exception("...")
}
case class MyCons1[A](val hd: A, val tl: MyList1[A]) extends MyList1[A]{
  override def getValue: Option[A] = Some(hd)
  override def getNext: MyList1[A] = tl
}
val t10 = MyCons1(3, MyCons1(5, MyCons1(7, MyNil1())))
sumElementsId(t10) // val res0: Int = 15

//
// Exercise
//
class IntCounter(n: Int) extends Iter[Int] {
  override def getValue: Option[Int] = n match{
    case _ if n<=0 => None
    case _ => Some(n)
  }
  override def getNext: Iter[Int] = new IntCounter(n-1)
}
sumElementsId(new IntCounter(100)) // val res1: Int = 5050

//
// A Better Interface
//
abstract class Iter2[A]{
  def get: Option[(A, Iter2[A])]
}
def sumElements2[A](f: A=>Int)(xs: Iter2[A]): Int=
  xs.get match{
    case None => 0
    case Some(n, tl)=> f(n) + sumElements2(f)(tl)
  }
def sumElementsId2 = sumElements2[Int]((x: Int)=>x) // def sumElementsId2: Iter2[Int] => Int\

// MyList2
sealed abstract class MyList2[A] extends Iter2[A]
case class MyNil2[A]() extends MyList2[A]{
  override def get: Option[(A, MyList2[A])] = None
}
case class MyCons2[A](val hd: A, val tl: MyList2[A]) extends MyList2[A]{
  override def get: Option[(A, MyList2[A])] = Some(hd, tl)
}

// IntCounter2
class IntCounter2(n: Int) extends Iter2[Int]{
  override def get: Option[(Int, IntCounter2)] =
    if n <= 0 then None else Some(n, new IntCounter2(n-1))
}