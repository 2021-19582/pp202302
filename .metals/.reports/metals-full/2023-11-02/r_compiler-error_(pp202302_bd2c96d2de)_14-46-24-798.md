file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/20231102.scala
### java.lang.IllegalArgumentException: Comparison method violates its general contract!

occurred in the presentation compiler.

action parameters:
offset: 2824
uri: file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/20231102.scala
text:
```scala
// trait Ord {
// // this cmp that < 0 iff this < that
// // this cmp that > 0 iff this > that
// // this cmp that == 0 iff this == that
// def cmp(that: Ord): Int
// def ===(that: Ord): Boolean = (this.cmp(that)) == 0
// def < (that: Ord): Boolean = (this cmp that) < 0
// def > (that: Ord): Boolean = (this cmp that) > 0
// def <= (that: Ord): Boolean = (this cmp that) <= 0
// def >= (that: Ord): Boolean = (this cmp that) >= 0
// }
// def max3(a: Ord, b: Ord, c: Ord) : Ord =
// if (a <= b) { if (b <= c) c else b }
// else { if (a <= c) c else a }


// class OInt(i: Int) extends Ord {
//     def cmp(that: Ord) : Int = i - that.asInstanceOf(Int) 
//     // we don't know what Ord type is! how can you subtract....
//     // by using .asInstanceOf(Int) we assure the program 'that' is Int
//     // kills the program if it is not Int

// /*** TRYME ***/
// trait Ord[A] {
//     def cmp(that: A): Int
//     def ===(that: A): Boolean = (this.cmp(that)) == 0
//     def < (that: A): Boolean = (this cmp that) < 0
//     def > (that: A): Boolean = (this cmp that) > 0
//     def <= (that: A): Boolean = (this cmp that) <= 0
//     def >= (that: A): Boolean = (this cmp that) >= 0
// }
// def max3[A <: Ord[A]](a: A, b: A, c: A) : A =
//     if (a <= b) {if (b <= c) c else b }
//     else {if (a <= c) c else a }

// class OInt(val value : Int) extends Ord[OInt] {
// def cmp(that: OInt) = value - that.value
// }

// // @main def test: Unit=
// //     println(max3(new OInt(3), new OInt(2), new OInt(10)).value)

// class Bag[U <: Ord[U]] protected (val toList: List[U]) {
//     // U must be comparible for this to work well
// def this() = this(Nil)
// def add(x: U) : Bag[U] = {
// def go(elmts: List[U]): List[U] =
// elmts match {
// case Nil => x :: Nil


/*** TRYME ***/
// completely separating Ord from int

trait Ord[A] {
def cmp(me: A, you: A): Int
def ===(me: A, you: A): Boolean = cmp(me,you) == 0
def < (me: A, you: A): Boolean = cmp(me,you) < 0
def > (me: A, you: A): Boolean = cmp(me,you) > 0
def <= (me: A, you: A): Boolean = cmp(me,you) <= 0
def >= (me: A, you: A): Boolean = cmp(me,you) >= 0
}
def max3[A](a: A, b: A, c: A)(implicit ord: Ord[A]) : A =
    if (ord.<=(a, b)) {if (ord.<=(b,c)) c else b }
    else {if (ord.<=(a,c)) c else a }
// implicit: find all implicit val within scope and take that-
// if we have intOrdRev with decreasing order, then implicit becomes ambiguous- error


implicit val intOrd : Ord[Int] = new Ord[Int] { 
    def cmp(me: Int, you: Int) = me - you 
}

// implicit val intOrdRev : Ord[Int] = new Ord[Int] {
//     def cmp(me: Int, you: Int) = you - me
// }

@main def test: Unit=
    println(max3(3,2,10)) // 10
    println(max3(3, 2, 10)(intOrd))
    val x = max3(3,2,10)
    println(x@@)

```



#### Error stacktrace:

```
java.base/java.util.TimSort.mergeLo(TimSort.java:781)
	java.base/java.util.TimSort.mergeAt(TimSort.java:518)
	java.base/java.util.TimSort.mergeForceCollapse(TimSort.java:461)
	java.base/java.util.TimSort.sort(TimSort.java:254)
	java.base/java.util.Arrays.sort(Arrays.java:1233)
	scala.collection.SeqOps.sorted(Seq.scala:727)
	scala.collection.SeqOps.sorted$(Seq.scala:719)
	scala.collection.immutable.List.scala$collection$immutable$StrictOptimizedSeqOps$$super$sorted(List.scala:79)
	scala.collection.immutable.StrictOptimizedSeqOps.sorted(StrictOptimizedSeqOps.scala:78)
	scala.collection.immutable.StrictOptimizedSeqOps.sorted$(StrictOptimizedSeqOps.scala:78)
	scala.collection.immutable.List.sorted(List.scala:79)
	scala.meta.internal.pc.completions.Completions.completions(Completions.scala:210)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:86)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:123)
```
#### Short summary: 

java.lang.IllegalArgumentException: Comparison method violates its general contract!