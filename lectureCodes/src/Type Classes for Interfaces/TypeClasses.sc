// Completely Separating Ord from Int
trait Ord[A] {
  def cmp(me: A, you: A): Int

  def === (me: A, you: A): Boolean = cmp(me, you) == 0
  def <   (me: A, you: A): Boolean = cmp(me, you) <  0
  def >   (me: A, you: A): Boolean = cmp(me, you) >  0
  def <=  (me: A, you: A): Boolean = cmp(me, you) <= 0
  def >=  (me: A, you: A): Boolean = cmp(me, you) >= 0
}
def max3[A](a: A, b: A, c: A)(implicit ord: Ord[A]): A =
  if (ord.<=(a, b)) { if (ord.<=(b, c)) c else b}
  else              { if (ord.<=(a, c)) c else b}
// behaves like Int, String, ..., A <: Ord in OOP

implicit val stringOrd : Ord[String] = new Ord[String] {
  def cmp(me: String, you: String) = me.toInt - you.toInt }

implicit val intOrd : Ord[Int] = new Ord[Int] {
  def cmp(me: Int, you: Int) = me - you }

max3("3","2","10") // val res0: String = 10

// "implicit" keyword assumes ord: Ord[String] is stringOrd and Ord[Int] is intOrd

implicit val intOrdMod3: Ord[Int] = new Ord[Int] {
  def cmp(me: Int, you: Int) = me%3 - you%3 }

//max3(3, 2, 10) // if there is more than one implicit val that is Ord[Int], then causes error
max3(3, 2, 10)(intOrd) // val res1: Int = 10
max3(3, 2, 10)(intOrdMod3) // val res2: Int = 2
// implicit arguments can also be given explicitly

//
// Implicit: an argument is given "implicitly"
//
def foo (s: String)(implicit t: String) = s + t
implicit val exclamation: String = "!!!!!!!!!!"
foo("Hi")
// val res3: String = Hi!!!!!!!!!!
foo("Hi")("??????????") // can be given explicitly
// val res4: String = Hi??????????

// Syntax for type class: syntactic sugar
trait Ord2[A]: // things for type Ord2
  extension(a:A) // things for instance a of A <: Ord2
    def cmp (b:A): Int
    def === (b:A) = a.cmp(b) == 0
    def <   (b:A) = a.cmp(b) <  0
    def >   (b:A) = a.cmp(b) >  0
    def <=  (b:A) = a.cmp(b) <= 0
    def >=  (b:A) = a.cmp(b) >= 0

def max3_2[A: Ord2](a: A, b: A, c: A): A =
  // A: Ord2 is similar to A<:Ord2
  if (a<=b) {
    if (b.<=(c)) c else b
  }
  else {
    if (a<=c) c else a
  }
//def max3_2[A](a: A, b: A, c: A)(implicit evidence$1: Ord2[A]): A

given intOrd2: Ord2[Int] with // implicit val intOrd2(a: Int)
  extension (a: Int) // a : Int <: Ord2[Int]
    def cmp(b: Int) = a - b
//implicit val intOrd2: Ord2[Int] = new Ord2[Int] {
//  extension (a: Int)
//    def cmp (b: Int): Int = a-b
//} // same thing!

// Bag Example using type class
class Bag2[A: Ord2] protected (val toList: List[A]) {
  // no need to use A <: Ord[A]
  def this()= this(Nil) // Nil extends List[Nothing]
  def add(x: A): Bag2[A] = {
    def loop(elmts: List[A]): List[A] =
      elmts match {
        case Nil => x :: Nil
        case e :: _ if (x < e) => x :: elmts
        case e :: _ if (x === e) => elmts
        case e :: rest => e :: loop(rest)
      }

    new Bag2(loop(toList))
  }
}
(new Bag2[Int]()).add(3).add(2).add(3).add(10).toList
// val res5: List[Int] = List(2, 3, 10)


// Bootstrapping Implicits
given tupOrd2[A, B](using Ord2[A], Ord2[B]): Ord2[(A, B)] with // with is like 'new'
  // using is identical to implicit
  extension (a: (A, B))
    def cmp(b: (A, B)): Int ={
      val c1 = a._1.cmp(b._1) // using Ord2[A] is required to use A.cmp
                              // also when actually performing, a given cmp function for type A must be noted
      if (c1 != 0) c1 else a._2.cmp(b._2)
    }

val b2 = new Bag2[(Int, (Int, Int))] // now we can compare all sorts of ILists!
b2.add((3, (3, 4))).add((3, (2, 7))).add((4, (0, 0))).toList
// val res6: List[(Int, (Int, Int))] = List((3,(2,7)), (3,(3,4)), (4,(0,0)))

// With Different Orders

// given intOrd : Ord[Int] with
// extension (a: Int)
// def cmp(b: Int) = a - b
def intOrdRev2 : Ord2[Int] = new {
  extension (a: Int)
    def cmp(b: Int) = b - a
}

(new Bag2[Int]()).add(3).add(2).add(10).toList
// val res7: List[Int] = List(2, 3, 10)
(new Bag2[Int]()(intOrdRev2)).add(3).add(2).add(10).toList
// val res8: List[Int] = List(10, 3, 2)