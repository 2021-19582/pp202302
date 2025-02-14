// import scala.annotation.tailrec
// import scala.util.control.TailCalls.TailRec
// // import reflect.Selectable.reflectiveSelectable

// // def bar (x: Int) = x+1

// // object foo // New!! an object (an onetime useable class; no constructors required)
// // { 
// //     val a = 1+2 // field
// //     def b = a + 1 // method
// //     def f(x: Int) = b + x
// //     val g : Int => Int = bar _ // closures (x)=>x+1 can be defined as val (ftns cannot)
// // }

// // @main def test1: Unit={
// //     println(foo.b) // a+1 ~ 3+1 ~ 4
// //     println(foo.f(3)) // b+3 ~ 3+1+3 ~ 7
// //     val ff : Int=>Int = foo.f _
// //     def g(x: {  // x is not foo- but an anonymous structural type
// //                 /*val a: Int; def b: Int;*/
// //                 def f(x:Int): Int/*; val g: Int => Int*/
// //                 /*; def c: Int*/
// //             }) // x is a structural type
// //             = x.f(3) // takes f of foo- x is 
// //     println(g(foo)) // if new method/field c is added, g cannot take foo
// //     println("updated3")
// // }

// // import reflect.Selectable.reflectiveSelectable

// // val gn = 0
// // object foo {
// //     val a = 3
// //     def b = a + 1
// //     def f(x: Int) = b + x + gn
// // }

// // type Foo = {val a: Int; def b: Int; def f(x:Int):Int} 
// // // New! a type, not an object;
// // // has values and definitions defined but not initiallized
// // def g(x: Foo) = {
// //     val gn = 10
// //     x.f(3)
// // }

// // @main def test2: Unit={
// //     println(foo.f(3))
// //     println(g(foo))
// //     println("updated")
// // }

// // sealed abstract class Attr
// // case class Name(name: String) extends Attr // New! algebraic datatype
// // case class Age(age: Int) extends Attr
// // case class DOB(year: Int, month: Int, day: Int) extends Attr
// // case class Height(height: Double) extends Attr

// // val a : Attr = Name("Chulsoo Kim")
// // val b : Attr = DOB(2000,3,10)

// // sealed abstract class IList
// // case class INil() extends IList // similar but more technical implementation of null
// // case class ICons(hd: Int, tl: IList) extends IList // you cannot extend multiple classes

// // val x : IList = ICons(2, ICons(1, INil()))

// // import scala.annotation.tailrec
// // import scala.util.control.TailCalls._


// // def gen(n: Int) : IList =
// //     if (n <= 0) INil()
// //     else ICons(n, gen(n-1))

// // def genCont(n: Int, cont: IList=>TailRec[IList]) : TailRec[IList]={ // CPS
// //     if (n <= 0) cont(INil())
// //     else {
// //         genCont(n-1, result => tailcall(cont(ICons(n, result))))
// //     }

// // }

// // // val y : IList = gen(10000000) // stack overflow

// // val y : IList = genCont(10000000, (x)=>done(x)).result
// // sealed abstract class BTree
// // case class Leaf() extends BTree
// // case class Node(value: Int, left: BTree, right: BTree) extends BTree

// // def length (xs: IList) : Int ={
// //     xs match{ // New! pattern matching
// //         case INil() => 0
// //         case ICons(x, tl) => 1 +length(tl)
// //     }
// // }

// // @tailrec
// // def lengthTail (xs: IList,acc: Int): Int={
// //     xs match{
// //         case INil() => acc
// //         case ICons(hd, tl) => lengthTail(tl, acc+1)
// //     }
// // }



// // @main def test3: Unit={
// //     println(length(x))
// //     println(lengthTail(y, 0))
// //     println("updated4")
// // }

// import scala.annotation.tailrec
// import scala.util.control.TailCalls._

// sealed abstract class IList[T]
// case class INil[T]() extends IList[T] // similar but more technical implementation of null
// case class ICons[T](hd: T, tl: IList[T]) extends IList[T] // you cannot extend multiple classes

// sealed abstract class IOption[T]
// case class INone[T]() extends IOption[T]
// case class ISome[T](x: T) extends IOption[T]

// def secondElmt[T](xs: IList[T]) : IOption[T] =
//     xs match {
//         case INil() | ICons(_,INil()) => INone()
//         case ICons(_, ICons(x, _)) => ISome(x)
//     }

// def secondElmt2[T](xs: IList[T]) : IOption[T] =
//     xs match {
//         case INil() | ICons(_,INil()) => INone()
//         case ICons(_, ICons(x, INil())) => ISome(x)
//         case _ => INone() // default; waterfalls as in switch-case of C
//     }

// def secondElmt3[T](xs: IList[T]) : IOption[T] =
//     xs match {
//         case ICons(_, ICons(x, INil())) => ISome(x)
//         case _ => INone() }


// def factorial(n: Long) : Long =
//     n match {
//         case 0 => 1
//         case _ => n * factorial(n-1)
//     }
// @tailrec
// def factorialTail(n: Long, acc: Long): Long={
//     n match{
//         case 1 => acc
//         case _ => factorialTail(n-1, acc*n)
//     }
// }

// def factorialCont(n: Long, cont: Long => TailRec[Long]): TailRec[Long]={
//     n match{
//         case 1 => cont(n)
//         case _ => factorialCont(n-1, result => tailcall(cont(n*result)))
//     }
// }

// def fib(n: Long): Long={
//     n match{
//         case 0 | 1 => 1
//         case _ => fib (n-1) + fib (n-2)
//     }
// }

// @tailrec
// def fibTail(n: Long, acc1: Long, acc2: Long): Long={
//     n match{
//         case 0 => acc1
//         case 1 => acc2
//         case _ => fibTail(n-1, acc2, acc1+acc2)
//     }
// }

// def fibCont(n: Long, cont: Long => TailRec[Long]): TailRec[Long]={
//     n match{
//         case 1|2 => cont(n)
//         case _ => tailcall(fibCont(n-1, result1=>
//             tailcall(fibCont(n-2, result2=>
//                 cont(result1+result2)))))
//     }
// }

// @main def test4: Unit={
//     println("updated4")
//     println(factorial(10L))
//     println(factorialTail(10L, 1L))
//     println(factorialCont(10L, (x)=>done(x)).result)

//     println(fib(20L))
//     println(fibTail(20L, 1L, 1L))
//     println(fibCont(20L, (x)=>done(x)).result)
// }