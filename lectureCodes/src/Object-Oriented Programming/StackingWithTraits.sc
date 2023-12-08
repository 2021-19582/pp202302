// Typical Hierarchy in Scala

//• BASE
//Interface (trait or abstract class)
//• CORE
//Functionality (trait or concrete class)
//• CUSTOM
//Modifications (each in a separate, composable trait

//
// IntStack
//

// Base
trait Stack[A] {
  def get(): (A, Stack[A])
  def put(x: Int): Stack[A]
}
// Core
class BasicIntStack protected (xs: List[Int]) extends Stack[Int]
{
  override val toString = "Stack: " + xs.toString()
  def this() = this(Nil)

  override def get(): (Int, Stack[Int]) = (xs.head, new BasicIntStack(xs.tail))
  override def put(x: Int): Stack[Int] = {
    print("Super ")
    (new BasicIntStack(x +: xs))
  }
//  Note that :-ending operators are right associative (see example). A mnemonic for +: vs. :+ is: the COLon goes on the COLlection side.
}

val s00 = new BasicIntStack
val s01 = s00.put(3)
val s02 = s01.put(-2)
val s03 = s02.put(4)
val (v01, s04) = s03.get()
//val v01: Int = 4
//val s04: Stack[Int] = Stack: List(-2, 3)
val (v02, s05) = s04.get()
//val v02: Int = -2
//val s05: Stack[Int] = Stack: List(3)

// Custom
trait Doubling extends Stack[Int]{
  abstract override def put(x: Int): Stack[Int] = {
    print("D ")
    super.put(2 * x)
  }
}

trait Incrementing extends Stack[Int] {
  abstract override def put(x: Int): Stack[Int] = {
    print("I ")
    super.put(x + 1)
  }
}

trait Filtering extends Stack[Int] {
  abstract override def put(x: Int): Stack[Int] = {
    print("F ")
    if (x >= 0) super.put(x) else this
  }
}

// Stacking
class DIFIntStack protected (xs: List[Int]) extends BasicIntStack(xs)
  with Doubling with Incrementing with Filtering{
  def this() = this(Nil)
} // Hmm I wonder which put type would be used?

val s10 = new DIFIntStack // s10: DIFIntStack = Stack: List()

val s11 = s10.put(3) // NOT DIFIntStack anymore
// F I D Super val s11: Stack[Int] = Stack: List(8)
// calls in rev order?
val s12 = s11.put(-2)
// Super val s12: Stack[Int] = Stack: List(-2, 8)
// ONLY SUPER?
val s13 = s12.put(4)
// Super val s13: Stack[Int] = Stack: List(4, -2, 8)
// ONLY SUPER

val (v11,s14) = s13.get()
val (v12,s15) = s14.get()
val (v13,s16) = s15.get()

println(s13) // Stack: List(4, -2, 8) // println(s13.toString)

//
// IntStack (Revision)
//

// Core
class BasicIntStack2 protected (xs: List[Int]) extends Stack[Int]
{
  override val toString = "Stack: " + xs.toString
  def this() = this(Nil)
  protected def mkStack(xs: List[Int]): Stack[Int] =
    new BasicIntStack2(xs)

  override def get(): (Int, Stack[Int]) = (xs.head, mkStack(xs.tail))
  override def put(x: Int): Stack[Int] = mkStack(x +: xs)
}

val s20 = new BasicIntStack2
val s21 = s20.put(3)
val s22 = s21.put(-2)
val s23 = s22.put(4)
val (v21,s24) = s23.get()
//val v21: Int = 4
//val s24: Stack[Int] = Stack: List(-2, 3)
val (v22,s25) = s24.get()
//val v22: Int = -2
//val s25: Stack[Int] = Stack: List(3)

// Stacking
class DIFIntStack2 protected (xs: List[Int]) extends BasicIntStack2(xs)
  with Doubling with Incrementing with Filtering
{
  def this() = this(Nil)
  override def mkStack(xs: List[Int]): DIFIntStack2 = new DIFIntStack2(xs)
}

val s30 = new DIFIntStack2
// val s30: DIFIntStack2 = Stack: List()
val s31 = s30.put(3)
// F I D val s31: Stack[Int] = Stack: List(8)
val s32 = s31.put(-2)
// F val s32: Stack[Int] = Stack: List(8)
val s33 = s32.put(4)
// F I D val s33: Stack[Int] = Stack: List(10, 8)
val (v31,s34) = s33.get()
//val v31: Int = 10
//val s34: Stack[Int] = Stack: List(8)
val (v32,s35) = s34.get()
//val v32: Int = 8
//val s35: Stack[Int] = Stack: List()
