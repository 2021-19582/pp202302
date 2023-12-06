//
// Multiple Inhertiance Problem
//
// The diamond problem
//class A (val a: Int)
//class B extends A(10)
//class C extends A(20)
//class D extends B, C
//
//what is the value of (new D).a ?
//
//the constructor of A must be executed once-
//or else A may contain side effects such as sending messages over network

// Java: Interface
// class can inherit only one parent, implement multiple interface

// Cpp:
// allows multiple inheritance, requires the programmer to take care of potential multiple inheritance problems

// Scala: Trait
// a trait can implement any of its methods
// but have only one constructor w/ no arguments
// a class/abstract class can extends one trait or abstravt class with any argu. w/ multiple traits T1, T2, ...
// such that for each i,
// the least superclass of Ti, if exists, should be a superclass of X
// where C is a superclass of T if C is an (abstract) class and T transitively “extends” C.
// No cyclic inheritance is allowed.

//Example
class A(val a : Int) {
  def this () = this(0)
}
trait B {
  def f(x: Int): Int = x
}
trait C extends A with B {
  def g(x: Int): Int = x + a
}
trait D extends B {
  def h(x: Int): Int = f(x + 50)
}
class E extends A(10) with C with D {
  override def f(x: Int) = x * a
}
val e = new E
e.f(1) // val res0: Int = 10
e.g(1) // val res1: Int = 11
e.h(1) // val res2: Int = 510


// Algorithm for Multiple Inheritance

//• Give a linear order among all ancestors by “post-order” traversing
//  without revisiting the same node.
//• Invoke the constructors once in that order.
//  Note. Post-order traversal of a class C means
//  − Recursively post-order traverse C’s first parent; …;
//  − Recursively post-order traverse C’s last parent; and
//  − Visit C.
//  By post-order traversing from “E” in the previous example,
//  we have the order: A(10) -> B -> C -> D -> E
//    val e = new E
//    e.a // 10                 e.f(100) // 100*10
//    e.g(100) // 100 + 10      e.h(100) // (100 + 50) * 10
//• A constructor with arguments is always visited before the same
//  constructor with no arguments.
//• Compile error if the same field is implemented by multiple classes
