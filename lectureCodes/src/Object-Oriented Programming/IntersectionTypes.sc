//
// Intersection Types
//
// t: T1 with T2 --> intersection type

// example
trait A {val a: Int = 0}
trait B {val b: Int = 0}
class C extends A with B {
  override val a: Int = 10
  override val b: Int = 20
  val c = 30
}
val x = new C
val y: A with B = x // y is an intersection type of A and B
y.a // val res0: Int = 10
y.b // val res1: Int = 20
//y.c // type error: value c is not a member of A & B

//
// Subtype Relation for "with"
//

//• Permutation
//===================================
//… with T1 with T2 … <: … with T2 with T1 …

//•Width
//=======================
//… with T … <: … …

//• Depth
//T <: S
//=======================
//… with T … <: … with S …
