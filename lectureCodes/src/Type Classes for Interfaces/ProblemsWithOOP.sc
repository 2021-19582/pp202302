// Subtype Polymorphism
trait Ord{
  // this cmp that < 0 iff this < that
  // this cmp that > 0 iff this > that
  // this cmp that == 0 iff this == that
  def cmp (that: Ord): Int

  def === (that: Ord): Boolean = (this.cmp(that)) == 0
  def < (that: Ord): Boolean = (this cmp that) < 0
  def >(that: Ord): Boolean = (this cmp that) > 0
  def <=(that: Ord): Boolean = (this cmp that) <= 0
  def >=(that: Ord): Boolean = (this cmp that) >= 0
}

def max3(a: Ord, b: Ord, c: Ord): Ord =
  if (a <= b) {
    if (b <= c) c else b
  }
  else {
    if (a <= c) c else a
  }

// Interface over Parameter Types
trait Ord1[A] {
  def cmp(that: A): Int
  def ===(that: A): Boolean = (this cmp(that)) == 0
  def <  (that: A): Boolean = (this cmp that) < 0
  def >  (that: A): Boolean = (this.cmp(that)) > 0
  def <= (that: A): Boolean = (this cmp that) <= 0
  def >= (that: A): Boolean = (this cmp that) >= 0
}

def max3_1[A <: Ord1[A]] (a: A, b: A, c: A): A =
  if (a <= b) { if (b <= c) c else b}
  else        { if (a <= c) c else a}

class OInt1(val value: Int) extends Ord1[OInt1] {
  def cmp(that: OInt1) = this.value - that.value
}

max3_1(new OInt1(3), new OInt1(2), new OInt1(10)).value // val res0: Int = 10

// Further Example: Ordered Bag
class Bag1[U <: Ord1[U]] protected (val toList: List[U]) {
  def this() = this(Nil)
  def add(x: U): Bag1[U] = {
    def go(elmts: List[U]): List[U]=
      elmts match {
        case Nil => x :: Nil
        case e :: _ if (x < e) => x :: elmts
        case e :: _ if (x === e) => elmts
        case e :: rest => e :: go(rest)
      }
    new Bag1(go(toList))
  }
}

val emp1 = new Bag1[OInt1]()
// val emp1: Bag1[OInt1] = Bag1@29723ea3
val b1 = emp1.add(new OInt1(3)).add(new OInt1(2)).
  add(new OInt1(10)).add(new OInt1(2))
// val b1: Bag1[OInt1] = Bag1@2a68cd7b

b1.toList.map((x)=>x.value)
// val res1: List[Int] = List(2, 3, 10)

//
// Problems with OOP
//

//  1. Needs “subtyping” like “OInt <: Ord[OInt]”, which is quite
//    complex as we have seen (and moreover, involves more
//    complex concepts like variance).
//  2. Needs a wrapper class like “OInt” in order to add a new
//    interface to an existing type like “Int”.
//  3. Interface only contains only “elimination” functions, not
//    “introduction” functions.
//  4. No canonical operator
//  5. …