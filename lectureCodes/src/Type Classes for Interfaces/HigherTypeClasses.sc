//
// Interfaces I
//

// eg.Iter[List]
trait Iter[I[_]]:           //  trait Iter[I, A]
  extension[A](i: I[A])     //    extension (i: I)
    def getValue: Option[A] //      def getValue: Option[A]
    def getNext: I[A]       //      def getNext: I

// eg. Iterable[MyTree]
trait Iterable[I[_]]:       //  trait Iterable
  type Itr[_]               //    type Itr
  given ItrI: Iter[Itr]     //    given ItrI: Iter[Itr, A]
  extension [A](i: I[A])    //    extension(i: I)
    def iter: Itr[A]        //      def iter: Itr

given iter2iterable[I[_]](using iterI: Iter[I]): Iterable[I] with
  type Itr[A] = I[A]
  def ItrI = iterI
  extension [A] (i: I[A])
    def iter = i

// Programs for Testing: use Iter, Iterable
def sumElements[I[_]](xs: I[Int])(implicit II: Iterable[I]) = {
  def loop(i: II.Itr[Int]): Int=
    i.getValue match{
      case None => 0
      case Some(n) => n + loop(i.getNext)
    }
  loop(xs.iter)
}

def printElements[I[A], A](xs: I[A])(f: A=>String)(implicit II: Iterable[I]) = {
//  println("HI")
  def loop(i: II.Itr[A]): Unit=
//    println("hey")
    i.getValue match{
      case None => {}
      case Some(n) => {
        println(f(n))
        loop(i.getNext)
      }
    }

  loop(xs.iter)
}

//
// Interfaces II
//
trait Listlike[L[_]]:
  extension[A](u: Unit)
    def unary_! : L[A]

  extension[A](elem: A)
    def :: (l: L[A]): L[A] // : usually toward list, + toward elmt
  extension[A](l:L[A])
    def head: Option[A]
    def tail: L[A]
    def ++(l2: L[A]): L[A]

trait Treelike[T[_]]:
  extension[A](u: Unit)
    def unary_! : T[A]

  extension[A](elem: A)
    def node(lt: T[A], rt: T[A]): T[A]

  extension[A](t: T[A])
    def root: Option[A]
    def left: T[A]
    def right: T[A]

// Programs for Testing: use All
def testList[L[_]](implicit ListI: Listlike[L], itrI: Iterable[L]) ={
  val l: L[Int]= (3 :: !()) ++ (1 :: 2:: !())
  println(sumElements(l))
  printElements(l)((x)=>x.toString)
}

def testTree[T[_]](implicit TreeI: Treelike[T], ItrI: Iterable[T]) ={
  val t = 3.node(4.node(!(), !()), 2.node(!(), !()))
  println(sumElements(t))
  printElements(t)((x)=>x.toString)
}

// List: provide Iter, ListIF
given listIter: Iter[List] with // // behaves like List[A] <: Iter[A] in OOP
  extension [A](l: List[A])
    def getValue = l.headOption
    def getNext = l.tail

given listListlike: Listlike[List] with // // behaves like List[A] <: Listlike[A] in OOP
  extension[A](u: Unit)
    def unary_! = Nil
  extension[A](a: A)
    def :: (l: List[A]) = a :: l
  extension[A](l: List[A])
    def head = l.headOption
    def tail = l.tail
    def ++(l2: List[A]) = l ::: l2 // merge list

// MyTree: use Iter, ListIF, provide Iterable, TreeIF
enum MyTree[+A]:
  case Leaf
  case Node(value: A, left: MyTree[A], right: MyTree[A])
import MyTree._

given treeIterable[L[_]](using listI: Listlike[L], iterI: Iter[L]): Iterable[MyTree]
with // behaves like MyTree[A] <: Iterable[A], but clumsy in OOP
  override type Itr[A] = L[A]
  override def ItrI: Iter[L] = iterI
  extension[A](t: MyTree[A])
    def iter: L[A] = t match{
      case Leaf => !()
      case Node(v, lt, rt) => v :: (lt.iter ++ rt.iter)
    }

given mytreeTreelike: Treelike[MyTree] with // behaves like MyTree[A] <: Treelike[A] in OOP
  extension[A](u: Unit)
    def unary_! = Leaf
  extension[A](a: A)
    def node(l: MyTree[A], r: MyTree[A]) = Node(a, l, r)
  extension[A](t: MyTree[A])
    def root = t match {
      case Leaf => None
      case Node(v, _, _) => Some(v)
    }
    def left = t match {
      case Leaf => t // technically meaningless end
      case Node(_,lt,_) => lt
    }
    def right = t match {
      case Leaf => t
      case Node(_,_,rt) => rt
    }

// Linking Modules
testList[List]
//6
//3
//1
//2
testTree[MyTree]
//9
//3
//4
//2

// List with Map
trait MapListlike[L[_]]:
  given LLI: Listlike[L]
  extension[A](l: L[A])
    def map[B](f: A=>B): L[B]

def testMapList[L[_]](implicit LI: MapListlike[L], IT: Iter[L])= {
  val l1 = 3.3 :: 2.2 :: 1.5 :: !()
  val l2 = l1.map((n: Double) => n.toInt)
  val l3 = l2.map((n: Int) => n.toString)
  printElements(l3)((x)=>x)
}

given listMapListlike (using lli: Listlike[List]): MapListlike[List] with
  def LLI = lli
  def ListlikeI = LLI
  extension [A] (l: List[A])
    def map[B](f: A=>B) = l.map(f)

testMapList[List]
//3
//2
//1