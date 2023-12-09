//
// Interfaces I: Elimination
//
trait Iter[I,A]:
  extension (i: I)
    def getValue: Option[A]
    def getNext: I

trait Iterable[I,A]:
  type Itr
  given ItrI: Iter[Itr,A]
  extension (i: I)
    def iter: Itr

// behaves like Iter[A] <: Iterable[A] in OOP
given iter2iterable[I,A](using iterI: Iter[I,A]): Iterable[I,A] with // with is basically 'new'
  type Itr = I
  def ItrI = iterI
  extension (i:I)
    def iter = i

// Programs for Testing: use Iter, Iterable
def sumElements[I](xs: I)(implicit II:Iterable[I,Int]) = {
  def loop(i: II.Itr): Int =
    i.getValue match {
      case None => 0
      case Some(n) => n + loop(i.getNext)
    }
  loop(xs.iter)
}

def printElements[I,A](xs: I)(implicit II: Iterable[I,A]) = {
  def loop(i: II.Itr): Unit =
    i.getValue match {
      case None =>
      case Some(a) => {println(a); loop(i.getNext)}
    }
  loop(xs.iter)
}


//
// Interfaces II: Introduction + Elimination
//
trait Listlike[L,A]:
  extension(u:Unit) // {} : Unit
    def unary_! : L
  extension(elem:A)
    def ::(l: L): L
  extension(l: L)
    def head: Option[A]
    def tail: L
    def ++(l2: L): L

trait Treelike[T,A]:
  extension(u:Unit)
    def unary_! : T
  extension(a: A)
    def node(lt: T, rt: T): T
  extension(t: T)
    def root : Option[A]
    def left : T
    def right : T

// Programs for Testing: use All
def testList[L](implicit ListI: Listlike[L,Int], ItrI: Iterable[L,Int]) = {
  val l = (3 :: !()) ++ (1 :: 2 :: !())
  println(sumElements(l)) //sumElements(l)(listIter[Int])
  printElements(l) //printElements(l)(listIter[Int])
}

def testTree[T](implicit TreeI: Treelike[T,Int], ItrI: Iterable[T,Int]) = {
  val t = 3.node(4.node(!(), !()), 2.node(!(),!()))
  println(sumElements(t))
  printElements(t)
}

// Implement Iter and Listlike for List

// behaves like List[A] <: Iter[A] in OOP
given listIter[A]: Iter[List[A],A] with
  extension (l: List[A])
    def getValue = l.headOption
    def getNext = l.tail

// behaves like List[A] <: Listlike[A] in OOP
given listListlike[A]: Listlike[List[A],A] with
  extension (u: Unit)
    def unary_! = Nil
  extension (a: A)
    def ::(l: List[A]) = a::l
  extension (l: List[A])
    def head = l.headOption
    def tail = l.tail
    def ++(l2: List[A]) = l ::: l2

// Implement Iterable for MyTree using Listlike,Iter

enum MyTree[+A]:
  case Leaf
  case Node(value: A, left: MyTree[A], right: MyTree[A])
import MyTree._

// behaves like MyTree[A] <: Iterable[A], but clumsy in OOP
given treeIterable[L,A](using listI: Listlike[L,A], iterI: Iter[L,A])
  : Iterable[MyTree[A], A] with
  type Itr = L
  def ItrI = iterI
  extension (t: MyTree[A])
    def iter: L = t match {
      case Leaf => !()
      case Node(v, lt, rt) => v :: (lt.iter ++ rt.iter)
    }

// Implement Treelike for MyTree
// behaves like MyTree[A] <: Treelike[A] in OOP
given mytreeTreelike[A] : Treelike[MyTree[A],A] with
  extension (u: Unit)
    def unary_! = Leaf
  extension (a: A)
    def node(l: MyTree[A], r: MyTree[A]) = Node(a,l,r)
  extension (t: MyTree[A])
    def root = t match {
      case Leaf => None
      case Node(v,_,_) => Some(v)
    }
    def left = t match {
      case Leaf => t
      case Node(_,lt,_) => lt
    }
    def right = t match {
      case Leaf => t
      case Node(_,_,rt) => rt }

// Linking Module
testList[List[Int]] // 6 3 1 2
testTree[MyTree[Int]] // 9 3 4 2