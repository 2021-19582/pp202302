import reflect.Selectable.reflectiveSelectable

//
// Nominal Sub Typing, a.k.a. Inheritance
class foo_type(x: Int, y: Int){
  val a: Int = x
  def b: Int = a + y
  def f(z: Int): Int = b + y + z
}

class gee_type(x: Int) extends foo_type(x+1, x+2) { // nominally defining gee_type <: foo_type
  val c: Int = f(x) + b
}

(new gee_type(30)).c

def test(f: foo_type) = f.a + f.b
test(new foo_type(10, 20))
test(new gee_type(30))

//
// Overriding
//
class foo_type1(x: Int, y: Int) {
  val a: Int = x
  def b: Int = 0
  def f(z: Int): Int = b * z
}
class gee_type1(x: Int) extends foo_type1(x+1, x+2) {
  override def b = 10
  // override def b = super.b + 10
  val c: Int = f(x) + b
}

(new gee_type1(30)).c
def test1(v: foo_type1) =
  println(v.f(42))

test1 (new foo_type1(1, 2))
test1 (new gee_type1(0)) // (x) => gee_type1(x) <: (x) => foo_type1(x+1, x+2)

//
// Overriding vs. Overloading
//
type foo_type2 = foo_type
object foo_type2{
  def apply(x: Int, y: Int) = new foo_type2(x, y)
}
class gee_type2(x:Int) extends foo_type2(x+1, x+2) {
  override def b = 10
//  override def f(z: String): Int = 77 // cannot override, arg: diff type
  def f(z: String): Int = 77 // Overloading, arg: diff type (different signature)
  override def f(z: Int): Int = 77 // Overriding, arg: same type (same signature)
}

//
// Example: MyList
//
class MyList[A] (v: A, nxt: Option[MyList[A]]) {
  val value: A = v
  val next: Option[MyList[A]] = nxt
}

type YourList[A] = Option[MyList[A]]
object YourList {
  def apply[A] (v: A, nxt: Option[MyList[A]]) = Some(new MyList(v, nxt))
}

val t: YourList[Int] =
  YourList(3, YourList(4, None))

class MyList2[A]()
// let's try w/o using Option[] (library)

class MyNil[A]() extends MyList2[A]
object MyNil { def apply[A]() = new MyNil[A]() } // Companion Object

class MyCons[A](val hd: A, val tl: MyList2[A])
  extends MyList2[A]
object MyCons { // Companion Object
  def apply[A](hd: A, tl: MyList2[A]) = new MyCons[A](hd, tl)
}

val t1: MyList2[Int] =
  new MyCons(3, new MyCons(4, new MyNil()))

val t2: MyList2[Int] = MyCons(3, MyCons(4, MyNil())) // after simplification w/ Companion Object

//def length(x: MyList2[Int]): Int =
//  x match{
//    case MyNil() => 0
//    case MyCons(hd, tl) => 1 + length(tl)
//  }
  // we cannot use pattern matching!! need to change to case class

//
// Case Class
//
sealed abstract class MyList3[A]()
case class MyNil3[A]() extends MyList3[A]
case class MyCons3[A](hd: A, tl: MyList3[A]) extends MyList3[A]

val t30: MyList3[Int] = MyCons3(3, MyCons3(4, MyNil3()))
// something we've done for a long- time
// algebraic datatypes

//
// Exercise: change MyTree[A] to SubClass
//
class MyTree[A] (val value: A, val left: Option[MyTree[A]], val right: Option[MyTree[A]])
//class MyTree[A] (v: A, lt: MyTree[A], rt: MyTree[A]){
//  val value = v
//  val left = lt
//  val right = rt
//}
object MyTree{
  def apply[A](v: A, lt: Option[MyTree[A]], rt: Option[MyTree[A]])= Option(new MyTree[A](v, lt, rt))
}

val tree0 = MyTree(3, None, None) // type mismatch
val tree1 = MyTree.apply[Int](3, None, None) // val tree: Option[MyTree[Int]]
// note that tree cannot be empty without assigning YourTree type
// SubClass
sealed abstract class MyTree4[A]
case class Empty4[A]() extends MyTree4[A]
case class Node4[A](value: A, left: MyTree4[A], right: MyTree4[A]) extends MyTree4[A]

val t40: MyTree4[Int] = Node4(3, Node4(4,Empty4(),Empty4()), Empty4())

t40 match{
  case Empty4() => 0
  case Node4(v, l, r) => v
} // val res4: Int = 3

//
// Solution with Monotonicity
//
// f[+A]: A1<:A2 => f[A1]<:f[A2]
// f[-A]: A1<:A2 => f[A2]<:f[A1]
sealed abstract class MyTree5[+A]
case object Empty5 extends MyTree5[Nothing] // Nothing is everything!
case class Node5[A] (value: A, left: MyTree5[A], right: MyTree5[A]) extends MyTree5[A]
val t50: MyTree5[Int] = Node5(3, Node5(4,Empty5,Empty5), Empty5) // instead of Empty() we can use Empty

//
// Solution with enum
//
enum MyTree6[+A]:
  case Empty6 // : MyTree[Nothing]
  case Node6(v: A, l: MyTree6[A], r: MyTree6[A])
import MyTree6._ // if we don't use import, we cannot use!

val t6 : MyTree6[Int] = Node6(3, Node6(4,Empty6,Empty6), Empty6)
// error w/o "import MyTree6._"
t6 match {
  case Empty6 => 0
  case Node6(v,l,r) => v
} // val res5: Int = 3

//
// Encoding ADT using classes: Option
//

//sealed abstract class MyOption7[+A]
//case class None7() extends MyOption7[Nothing]
//case class Some7[A](v: A) extends MyOption7[A]
sealed abstract class MyOption7[+A]{
  def matches[R] (e1: =>R, e2: A=>R): R
}
object MyNone7 extends MyOption7[Nothing] {
  override def matches[R](e1: => R, e2: Nothing=> R): R = e1
}
class MySome7[A](v: A) extends MyOption7[A]{
  override def matches[R](e1: => R, e2: A=>R): R = e2(v)
}
object MySome{
  def apply[A](v: A) = new MySome7(v)
}

val x70: MyOption7[Int] = MySome(42)
val y70: MyOption7[Int] = MyNone7
x70.matches(-1, (n)=>n+1) // val res6: Int = 43
y70.matches(-1, (n)=>n+1) // val res7: Int = -1

//
// Encoding ADT using classes: List
//
sealed abstract class MyList8[+A]{
  def matches[R](nilE: =>R, consE: (A, MyList8[A])=>R): R
}

object MyNil8 extends MyList8[Nothing]{
  override def matches[R](nilE: => R, consE: (Nothing, MyList8[Nothing]) => R): R = nilE
}

class MyCons8[A](v: A, tl: MyList8[A]) extends MyList8[A]{
  override def matches[R](nilE: => R, consE: (A, MyList8[A]) => R): R = consE(v, tl)
}

object MyCons8{
  def apply[A](v: A, tl: MyList8[A]) = new MyCons8[A](v, tl)
}

def length8[A](l: MyList8[A]): Int=
  l.matches(0, (v:A, tl:MyList8[A])=>1+length8(tl))

length8(MyCons8(3, MyCons8(2, MyNil8))) // val res8: Int = 2

//
// Encoding ADT using classes: Monotonicity
//
sealed abstract class MyList9[+A]{
  def matches[R](nilE: => R, consE: (A, MyList9[A])=>R): R
  def append[B>:A](l: MyList9[B]): MyList9[B]
}
object MyNil9 extends MyList9[Nothing]{
  override def matches[R](nilE: => R, consE: (Nothing, MyList9[Nothing]) => R): R = nilE
  override def append[B >: Nothing](l: MyList9[B]): MyList9[B] = l
}
class MyCons9[A](val hd: A, val tl: MyList9[A]) extends MyList9[A]{
  override def matches[R](nilE: => R, consE: (A, MyList9[A]) => R): R = consE(hd, tl)
  override def append[B >: A](l: MyList9[B]): MyList9[B] = new MyCons9[B](hd, tl.append(l))
}
object MyCons9{
  def apply[A](hd: A, tl: MyList9[A]) = new MyCons9[A](hd,tl)
}

def length9[A](l: MyList9[A]): Int={
  l.matches(0, (_, tl)=> 1+ length9(tl))
}

length9(MyCons9(3, MyCons9(2, MyNil9))) // val res9: Int = 2

def printer9[Int](l: MyList9[Int]): Unit= {
  l.matches(println("the end"), (hd, tl) => {
    println(hd)
    printer9(tl)
  })
}

printer9(MyCons9(3, MyCons9(2, MyNil9)))
//3
//2
//the end

printer9(MyCons9(3, MyCons9(2, MyNil9)).append(MyNil9))
//3
//2
//the end

printer9(MyNil9.append(MyCons9(3, MyCons9(2, MyNil9))))
//3
//2
//the end

printer9(MyCons9(3, MyCons9(2, MyNil9).append(MyCons9(1, MyCons9(0, MyNil9)))))
//3
//2
//1
//0
//the end