//
// Motivation: Mixin Functionality
//
abstract class Iter[A] {
  def getValue: Option[A]
  def getNext: Iter[A]
}

class ListIter[A](val list: List[A]) extends Iter[A]{
  override def getValue: Option[A] = list.headOption
  override def getNext: ListIter[A] = new ListIter(list.tail)
}

// Mixin Composition
trait MRIter[A] extends Iter[A] {
  override def getNext: MRIter[A] // required to transform to MRIter instead of Iter
  def mapReduce[B,C](combine: (B, C)=>C, ival: C, f: A=>B): C =
    getValue match {
      case None => ival
      case Some(v) => combine(f(v), getNext.mapReduce(combine, ival, f))
    }
}

class MRListIter[A](list: List[A]) extends ListIter(list) with MRIter[A] {
  override def getNext: MRListIter[A] = new MRListIter(super.getNext.list)
                                        // new MRListIter(list.tail)
}
val mr = new MRListIter[Int](List(3, 4, 5))
mr.mapReduce[Int, Int]((b, c)=> b+c, 0, (a)=>a*a) // val res0: Int = 50

// Mixin Composition: A Better Way
trait MRIter2[A] extends Iter[A] {
  def mapReduce[B,C](combine: (B, C)=>C, ival: C, f: A=>B): C ={
    def loop(c: Iter[A]): C = c.getValue match {
      case None => ival
      case Some(v) => combine(f(v), loop(c.getNext))
    }
    loop(this)
  }
}

class MRListIter2[A](list: List[A]) extends ListIter(list) with MRIter2[A]

val mr2 = new MRListIter2[Int](List(3, 4, 5))
mr2.mapReduce[Int, Int]((b, c)=>b+c, 0, (a)=> a*a) // val res1: Int = 50
// 3*3 + 4*4 + 5*5 = 50

// Syntactic Sugar: new A with B with C {...}
//
//  new A(...) with B1 ... with Bm {
//    code
//  }
//
//is equivalent to
//
//  {
//    class _tmp_(args) extends A(args) with B1 ... with Bm {
//      code
//    }
//    new _tmp_(...)
//  }