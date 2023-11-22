import reflect.Selectable.reflectiveSelectable

//
// Structural Types (a.k.a. Record Types): Examples
//
def bar (x: Int) = x+1
object foo{ // basically a tuple instance
  val a = 1+2
  def b = a+1
  def f(x: Int) = b+x
  val g: Int => Int = bar _ // needs closure to assign to val
}

foo.b // val res0: Int = 4
foo.f(3) // val res1: Int = 7

val ff: Int=>Int = foo.f _
// val ff: Int => Int = Lambda$3788/0x000001ba90fcc600@6324f62b

def g( // tuple type => Int
       x: { // tuple data type
         val a: Int;
         def b: Int;
         def f(x: Int):Int;
         val g: Int=>Int
       }
     )= x.f(3)
// def g
//  (x: Object{val a: Int; def b: Int; def f(x: Int): Int; val g: Int => Int}):
//    Int

g(foo) // val res2: Int = 7

//
// Type Alias
//
val gn = 0
object foo2 {
  val a = 3
  def b = a + 1
  def f(x: Int) = b + x + gn
}

foo2.f(3)

type Foo = {val a: Int; def b: Int; def f(x:Int):Int} // a type, NOT an instance
// defined alias type Foo = Object{val a: Int; def b: Int; def f(x: Int): Int}

def g2(x: Foo) = { // g2: Foo => Int
  val gn = 10
  x.f(3)
}
g2(foo2) // val res4: Int = 7
// we did not make foo2 as an instance of Foo
// but is each element is automatically assigned
// corresponding to the NAME OF PARAMETER

//object foo2_1{
//  val a_1 = 3
//  def b_1 = a_1 + 1
//  def f_1(x: Int) = b_1 + x + gn
//}

//g2(foo2_1) // does not work!
// [E007] Type Mismatch Error

//
// Algebraic Datatypes
//
sealed abstract class Attr
case class Name(name: String) extends Attr
case class Age(age: Int) extends Attr
case class DOB(year: Int, month: Int, height: Int) extends Attr
case class Height(height: Double) extends Attr

val a: Attr = Name("Chulsoo Kim")
val b: Attr = DOB(2000,3,10)


sealed abstract class IList
case class INil() extends IList
case class ICons(hd: Int, tl: IList) extends IList

val x : IList = ICons(2, ICons(1, INil()))
def gen(n: Int) : IList =
  if (n <= 0) INil()
  else ICons(n, gen(n-1))

// node
sealed abstract class IOption
case class INone() extends IOption // acts as null in Java
case class ISome(some: Int) extends IOption

// binary tree
sealed abstract class BTree
case class Leaf() extends BTree
case class Node(value: Int, left: BTree, right: BTree) extends BTree

//
// Pattern Matching
//
def length(xs: IList) : Int =
  xs match {
    case INil() => 0
    case ICons(x, tl) => 1 + length(tl)
  }
length(ICons(3, ICons(2, ICons(1, INil())))) // val res5: Int = 3

def secondElmt(xs: IList) : IOption =
  xs match {
    case INil() | ICons(_,INil()) => INone()
    case ICons(_, ICons(x, _)) => ISome(x)
  }

def secondElmt2(xs: IList) : IOption =
  xs match {
    case INil() | ICons(_,INil()) => INone()
    case ICons(_, ICons(x, INil())) => ISome(x) // only matches if length(xs) = 2
    case _ => INone() // default is INone()
  }

def secondElmt3(xs: IList) : IOption =
  xs match {
    case ICons(_, ICons(x, INil())) => ISome(x) // only matches if length(xs) = 2
    case _ => INone()
  }

secondElmt(ICons(3, ICons(2, ICons(1, INil())))) // val res6: IOption = ISome(2)
secondElmt2(ICons(3, ICons(2, ICons(1, INil())))) // val res7: IOption = INone()
secondElmt3(ICons(3, ICons(2, ICons(1, INil())))) // val res8: IOption = INone()

def secondElmt4(xs: IList) : IOption =
  xs match{
    case ICons(_, ICons(x, _)) => ISome(x)
    case _ => INone()
  }
secondElmt4(ICons(3, ICons(2, ICons(1, INil())))) // val res8: IOption = INone()

def factorial(n: Int) : Int =
  n match { // match can be used just like switch-cases
    case 0 => 1
    case _ => n * factorial(n-1)
  }
def fib(n: Int) : Int =
  n match {
    case 0 | 1 => 1
    case _ => fib(n-1) + fib(n-2)
  }

//fib(100) // still takes a long time... related to recursive implementation of fibo

def f(n: Int) : Int =
  n match {
    case 0 | 1 => 1
    case _ if (n <= 5) => 2
    case _ => 3
  }
def f(t: BTree) : Int =
  t match {
    case Leaf() => 0
    case Node(n,_,_) if (n <= 10) => 1
    case Node(_,_,_) => 2
  }

def find(t: BTree, x: Int): Boolean =
  t match {
    case Leaf() => false
    case Node(n,left,right) =>
      if (n == x) true
      else (find(left, x) || find(right, x))
  }
def find2(t: BTree, x: Int): Boolean =
  t match {
    case Leaf() => false
    case Node(n,_,_) if n==x => true // note the position of => within the line
    case Node(_,l,r) => find(l,x)||find(r,x)
  }

def t: BTree = Node(5,Node(4,Node(2,Leaf(),Leaf()),
                              Leaf()),
                      Node(7,Node(6,Leaf(),Leaf()),
                              Leaf()))
//    5
//   / \
//  4   7
// /   /
//2   6

find(t,7)
find2(t,7)
find(t,1)
find2(t,1)
