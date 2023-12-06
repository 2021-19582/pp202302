{
  //
  // Problem: Inefficiency
  //
  def time[R](block: => R): R = {
    val t0 = System.nanoTime()
    val result = block // call-by-name
    val t1 = System.nanoTime()
    println("Elapsed time: " + (t1 - t0) / 1000000 + "ms")
    result
  }

  abstract class Iter[+A] extends Iterable[A] {
    def getValue: Option[A]

    def getNext: Iter[A]

    def iter = this
  }

  abstract class Iterable[+A] {
    def iter: Iter[A]
  }

  sealed abstract class MyList[+A] extends Iter[A]
  case class MyNil[+A]() extends MyList[A] {
    override def getValue: Option[A] = None

    override def getNext: MyList[A] = this
  }
  case class MyCons[+A](hd: A, tl: MyList[A]) extends MyList[A] {
    override def getValue: Option[A] = Some(hd)

    override def getNext: MyList[A] = tl
  }

  sealed abstract class MyTree[+A] extends Iterable[A] {
    override def iter: MyList[A]
  }
  case class Empty[+A]() extends MyTree[A] {
    override def iter: MyList[A] = new MyNil[A] // new MyNil[A]() is also ok
  }
  case class Node[+A](v: A, lt: MyTree[A], rt: MyTree[A]) extends MyTree[A] {
    override def iter: MyList[A] =
      def merge_right(lt: MyTree[A]): MyTree[A] =
        lt match {
          case Empty() => rt
          case Node(lv, llt, lrt) => new Node(lv, llt, merge_right(lrt))
        }

      merge_right(lt) match {
        case Empty() => new MyCons[A](v, rt.iter)
        case Node(lv, llt, lrt) => new MyCons[A](v, merge_right(lt).iter)
      }
  }

  def generateTree(n: Int): MyTree[Int] = {
    def gen(lo: Int, hi: Int): MyTree[Int] =
      if (lo > hi) Empty()
      else {
        val mid = (lo + hi) / 2
        Node(mid, gen(lo, mid - 1), gen(mid + 1, hi))
      }

    gen(1, n)
  }


  // Problem: takes a few seconds to get a single value
  def sumN[A](f: A => Int)(n: Int, xs: Iterable[A]): Int = {
    def sumIter(res: Int, n: Int, xs: Iter[A]): Int =
      if (n <= 0) res
      else xs.getValue match {
        case None => res
        case Some(v) => sumIter(f(v) + res, n - 1, xs.getNext)
      }

    sumIter(0, n, xs.iter)
  }


  {
    val t: MyTree[Int] = generateTree(2000) // if n becomes larger, terminated by stack overflow
    time(sumN((x: Int) => x)(2000, t)) // val res0: Int = 2001000
    // too slow!
  }

  //
  // Solution1: Using List of Trees; Lazy Iteration
  //

  sealed abstract class MyTree1[+A] extends Iterable[A]
  class MyTreeIter1[A](val lst: MyList[MyTree1[A]]) extends Iter[A] {
    // a list of mytrees                                                         
    val getValue = lst match
      case MyCons(Node1(v, _, _), _) => Some(v)
      case _ => None

    def getNext =
      val remainingTrees: MyList[MyTree1[A]] = lst match
        case MyNil() => throw new Exception("...")
        case MyCons(hd, tl) => hd match
          case Empty1() => throw new Exception("...")
          case Node1(_, Empty1(), Empty1()) => tl
          case Node1(_, lt, Empty1()) => MyCons(lt, tl)
          case Node1(_, Empty1(), rt) => MyCons(rt, tl)
          case Node1(_, lt, rt) => MyCons(lt, MyCons(rt, tl))
      new MyTreeIter1(remainingTrees)
  }

  case class Empty1[+A]() extends MyTree1[A] {
    override val iter = new MyTreeIter1(MyNil())
  }
  case class Node1[A](v: A, lt: MyTree1[A], rt: MyTree1[A]) extends MyTree1[A] {
    override val iter = new MyTreeIter1[A](MyCons(this, MyNil()))
  }

  def generateTree1(n: Int): MyTree1[Int] = {
    def gen(lo: Int, hi: Int): MyTree1[Int] =
      if (lo > hi) Empty1()
      else {
        val mid = (lo + hi) / 2
        Node1(mid, gen(lo, mid - 1), gen(mid + 1, hi))
      }

    gen(1, n)
  }
  {
    val t1: MyTree1[Int] = generateTree1(2000)
    time (sumN((x: Int)=>x)(2000, t1)) // val res1: Int = 2001000
  }

  //
  // Solution2: Lazy List
  //
  sealed abstract class LazyList[A] extends Iter[A] {
    def append(lst: LazyList[A]): LazyList[A]
  }
//  sealed abstract class LazyList[+A] extends Iter[A]{
//    def append(lst: LazyList[A]): LazyList[A]
//  } // covariant type is not allowed
  case class LNil[A]() extends LazyList[A]{
    override def getValue: Option[A] = None
    override def getNext: Iter[A] = this
    override def append(lst: LazyList[A]): LazyList[A] = lst
  }
  case class LCons[A](hd: A, _tl : LazyList[A]) extends LazyList[A]{ /// ??? _tl :=>LazyList[A] doesn't work...
    lazy val tl = _tl
    override def getValue: Option[A] = Some(hd)
    override def getNext: Iter[A] = tl

    override def append(lst: LazyList[A]): LazyList[A] = LCons(hd, tl.append(lst))
    // add lst inside the original list
  }

  sealed abstract class MyTree2[A] extends Iterable[A]{
    def iter : LazyList[A]
  }
  case class Empty2[A]() extends MyTree2[A]{
    val iter = LNil()
  }
  case class Node2[A](hd: A, lt: MyTree2[A], rt: MyTree2[A]) extends MyTree2[A] {
    lazy val iter = LCons(hd, lt.iter.append(rt.iter))
      // lazy val iter = lt.iter.append(LCons(hd,rt.iter))
      // lazy val iter = lt.iter.append(rt.iter.append(
      // LCons(hd,LNil())))
  }
  def generateTree2(n: Int): MyTree2[Int] = {
    def gen(lo: Int, hi: Int): MyTree2[Int] =
      if (lo > hi) Empty2()
      else {
        val mid = (lo + hi) / 2
        Node2(mid, gen(lo, mid - 1), gen(mid + 1, hi))
      }

    gen(1, n)
  }
  {
    val t2: MyTree2[Int] = generateTree2(2000)
    time (sumN((x: Int)=> x)(200, t2)) // val res2: Int = 21283
  }
}