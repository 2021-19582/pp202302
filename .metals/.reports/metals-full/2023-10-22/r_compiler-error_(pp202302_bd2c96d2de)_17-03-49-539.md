file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main2.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 3323
uri: file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main2.scala
text:
```scala
// import reflect.Selectable.reflectiveSelectable

// def bar (x: Int) = x+1

// object foo // New!! an object (an onetime useable class; no constructors required)
// { 
//     val a = 1+2 // field
//     def b = a + 1 // method
//     def f(x: Int) = b + x
//     val g : Int => Int = bar _ // closures (x)=>x+1 can be defined as val (ftns cannot)
// }

// @main def test1: Unit={
//     println(foo.b) // a+1 ~ 3+1 ~ 4
//     println(foo.f(3)) // b+3 ~ 3+1+3 ~ 7
//     val ff : Int=>Int = foo.f _
//     def g(x: {  // x is not foo- but an anonymous structural type
//                 /*val a: Int; def b: Int;*/
//                 def f(x:Int): Int/*; val g: Int => Int*/
//                 /*; def c: Int*/
//             }) // x is a structural type
//             = x.f(3) // takes f of foo- x is 
//     println(g(foo)) // if new method/field c is added, g cannot take foo
//     println("updated3")
// }

// import reflect.Selectable.reflectiveSelectable

// val gn = 0
// object foo {
//     val a = 3
//     def b = a + 1
//     def f(x: Int) = b + x + gn
// }

// type Foo = {val a: Int; def b: Int; def f(x:Int):Int} 
// // New! a type, not an object;
// // has values and definitions defined but not initiallized
// def g(x: Foo) = {
//     val gn = 10
//     x.f(3)
// }

// @main def test2: Unit={
//     println(foo.f(3))
//     println(g(foo))
//     println("updated")
// }

// sealed abstract class Attr
// case class Name(name: String) extends Attr // New! algebraic datatype
// case class Age(age: Int) extends Attr
// case class DOB(year: Int, month: Int, day: Int) extends Attr
// case class Height(height: Double) extends Attr

// val a : Attr = Name("Chulsoo Kim")
// val b : Attr = DOB(2000,3,10)

// sealed abstract class IList
// case class INil() extends IList // similar but more technical implementation of null
// case class ICons(hd: Int, tl: IList) extends IList // you cannot extend multiple classes

// val x : IList = ICons(2, ICons(1, INil()))

// import scala.annotation.tailrec
// import scala.util.control.TailCalls._


// def gen(n: Int) : IList =
//     if (n <= 0) INil()
//     else ICons(n, gen(n-1))

// def genCont(n: Int, cont: IList=>TailRec[IList]) : TailRec[IList]={ // CPS
//     if (n <= 0) cont(INil())
//     else {
//         genCont(n-1, result => tailcall(cont(ICons(n, result))))
//     }

// }

// // val y : IList = gen(10000000) // stack overflow

// val y : IList = genCont(10000000, (x)=>done(x)).result
// sealed abstract class BTree
// case class Leaf() extends BTree
// case class Node(value: Int, left: BTree, right: BTree) extends BTree

// def length (xs: IList) : Int ={
//     xs match{ // New! pattern matching
//         case INil() => 0
//         case ICons(x, tl) => 1 +length(tl)
//     }
// }

// @tailrec
// def lengthTail (xs: IList,acc: Int): Int={
//     xs match{
//         case INil() => acc
//         case ICons(hd, tl) => lengthTail(tl, acc+1)
//     }
// }



// @main def test3: Unit={
//     println(length(x))
//     println(lengthTail(y, 0))
//     println("updated4")
// }

sealed abstract class IList[T]
case class INil() extends IList // similar but more technical implementation of null
case class ICons(hd: In[@@t, tl: IList) extends IList // you cannot extend multiple classes

sealed abstract class IOption[T]
case class INone[T]() extends IOption[T]
case class ISome[T](x: T) extends IOption[T]

def secondElmt(xs: IList) : IOption =
    xs match {
        case INil() | ICons(_,INil()) => INone()
        case ICons(_, ICons(x, _)) => ISome(x)
    }

def secondElmt2(xs: IList) : IOption =
    xs match {
        case INil() | ICons(_,INil()) => INone()
        case ICons(_, ICons(x, INil())) => ISome(x)
        case _ => INone() // default; waterfalls as in switch-case of C
    }

def secondElmt3(xs: IList) : IOption =
xs match {
case ICons(_, ICons(x, INil())) => ISome(x)
case _ => INone() }
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2576)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:94)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner