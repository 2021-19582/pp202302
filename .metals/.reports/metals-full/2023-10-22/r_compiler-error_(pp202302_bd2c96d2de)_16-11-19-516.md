file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main2.scala
### java.lang.AssertionError: assertion failed

occurred in the presentation compiler.

action parameters:
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

sealed abstract class Attr
case class Name(name: String) extends Attr // New! algebraic datatype
case class Age(age: Int) extends Attr
case class DOB(year: Int, month: Int, day: Int) extends Attr
case class Height(height: Double) extends Attr

val a : Attr = Name("Chulsoo Kim")
val b : Attr = DOB(2000,3,10)

sealed abstract class IList
case class INil() extends IList // similar but more technical implementation of null
case class ICons(hd: Int, tl: IList) extends IList // you cannot extend multiple classes

val x : IList = ICons(2, ICons(1, INil()))

def gen(n: Int) : IList =
    if (n <= 0) INil()
    else ICons(n, gen(n-1))

val
sealed abstract class BTree
case class Leaf() extends BTree
case class Node(value: Int, left: BTree, right: BTree) extends BTree

def length (xs: IList) : Int ={
    xs match{ // New! pattern matching
        case INil() => 0
        case ICons(x, tl) => 1 +length(tl)
    }
}

@main def test3: Unit={
    println(length(x))
    println("updated")
}
```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:11)
	dotty.tools.dotc.core.Annotations$LazyAnnotation.tree(Annotations.scala:136)
	dotty.tools.dotc.core.Annotations$Annotation$Child$.unapply(Annotations.scala:242)
	dotty.tools.dotc.typer.Namer.insertInto$1(Namer.scala:477)
	dotty.tools.dotc.typer.Namer.addChild(Namer.scala:488)
	dotty.tools.dotc.typer.Namer$Completer.register$1(Namer.scala:899)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild$$anonfun$1(Namer.scala:908)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild(Namer.scala:908)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:811)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:174)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:187)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:189)
	dotty.tools.dotc.core.Types$NamedType.info(Types.scala:2308)
	dotty.tools.dotc.core.Types$TermLambda.dotty$tools$dotc$core$Types$TermLambda$$_$compute$1(Types.scala:3791)
	dotty.tools.dotc.core.Types$TermLambda.foldArgs$2(Types.scala:3798)
	dotty.tools.dotc.core.Types$TermLambda.dotty$tools$dotc$core$Types$TermLambda$$_$compute$1(Types.scala:4409)
	dotty.tools.dotc.core.Types$TermLambda.dotty$tools$dotc$core$Types$TermLambda$$depStatus(Types.scala:3818)
	dotty.tools.dotc.core.Types$TermLambda.dependencyStatus(Types.scala:3832)
	dotty.tools.dotc.core.Types$TermLambda.isResultDependent(Types.scala:3854)
	dotty.tools.dotc.core.Types$TermLambda.isResultDependent$(Types.scala:3748)
	dotty.tools.dotc.core.Types$MethodType.isResultDependent(Types.scala:3892)
	dotty.tools.dotc.typer.TypeAssigner.assignType(TypeAssigner.scala:288)
	dotty.tools.dotc.typer.TypeAssigner.assignType$(TypeAssigner.scala:16)
	dotty.tools.dotc.typer.Typer.assignType(Typer.scala:115)
	dotty.tools.dotc.ast.tpd$.Apply(tpd.scala:49)
	dotty.tools.dotc.ast.tpd$TreeOps$.appliedToTermArgs$extension(tpd.scala:935)
	dotty.tools.dotc.ast.tpd$.New(tpd.scala:521)
	dotty.tools.dotc.ast.tpd$.New(tpd.scala:512)
	dotty.tools.dotc.core.Annotations$Annotation$Child$.makeChildLater$1(Annotations.scala:231)
	dotty.tools.dotc.core.Annotations$Annotation$Child$.later$$anonfun$1(Annotations.scala:234)
	dotty.tools.dotc.core.Annotations$LazyAnnotation.tree(Annotations.scala:140)
	dotty.tools.dotc.core.Annotations$Annotation$Child$.unapply(Annotations.scala:242)
	dotty.tools.dotc.typer.Namer.insertInto$1(Namer.scala:477)
	dotty.tools.dotc.typer.Namer.addChild(Namer.scala:488)
	dotty.tools.dotc.typer.Namer$Completer.register$1(Namer.scala:899)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild$$anonfun$1(Namer.scala:908)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.Namer$Completer.registerIfChild(Namer.scala:908)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:811)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:174)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:187)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:189)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:390)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:2869)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:2894)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2990)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3058)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3062)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3084)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3130)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2692)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:2961)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:2991)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3058)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3062)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3174)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:44)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$adapted$1(TyperPhase.scala:54)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:437)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:54)
	dotty.tools.dotc.typer.TyperPhase.runOn$$anonfun$3(TyperPhase.scala:88)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:88)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:247)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1321)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:263)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:271)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:280)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:67)
	dotty.tools.dotc.Run.compileUnits(Run.scala:280)
	dotty.tools.dotc.Run.compileSources(Run.scala:195)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:165)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:42)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:81)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:99)
```
#### Short summary: 

java.lang.AssertionError: assertion failed