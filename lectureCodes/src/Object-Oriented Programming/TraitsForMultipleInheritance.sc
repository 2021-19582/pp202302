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
// a trait can implement any of its methods but