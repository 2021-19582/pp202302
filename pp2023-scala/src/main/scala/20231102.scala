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

val intOrdRev : Ord[Int] = new Ord[Int] {
    def cmp(me: Int, you: Int) = you - me
}

// @main def test: Unit=
//     println(max3(3,2,10)) // 10
//     println(max3(3, 2, 10)(intOrd))
//     println(max3(3, 2, 10)(intOrdRev)) // gives minimum
    
//     // val x = max3(3,2,10)
//     // println(x(intOrd)) // this does not work



// class Bag[A : Ord] protected (val toList: List[A])
// { 
//     def this() = this(Nil)
//     def add(x: A) : Bag[A] = {
//         def go(elmts: List[A]) : List[A] =
//         elmts match {
//             case Nil => x :: Nil
//             case e :: _ if (implicitly[Ord[A]].<(x,e)) => x :: elmts
//             case e :: _ if (implicitly[Ord[A]].===(x,e)) => elmts
//             case e :: rest => e :: go(rest)
//         }
//         new Bag(go(toList))
//     }
// }

// implicit def tupOrd[A, B](implicit ordA: Ord[A], ordB: Ord[B]) 
// : Ord[(A, B)] =
//     new Ord[(A, B)] {
//     def cmp(me: (A, B), you: (A, B)) : Int = {
//     val c1 = ordA.cmp(me._1, you._1)
//     if (c1 != 0) c1
//     else { ordB.cmp(me._2, you._2) }
//     }
//     }
// val b = new Bag[(Int,(Int,Int))]

// @main def test: Unit =
//     println((new Bag[Int]()).add(3).add(2).add(3).add(10).toList)
//     println(b.add((3,(3,4))).add((3,(2,7))).add((4,(0,0))).toList)