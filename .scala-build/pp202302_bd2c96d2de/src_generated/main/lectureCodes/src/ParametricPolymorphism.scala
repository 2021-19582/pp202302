
package lectureCodes.src



final class ParametricPolymorphism$_ {
def args = ParametricPolymorphism_sc.args$
def scriptPath = """lectureCodes/src/ParametricPolymorphism.sc"""
/*<script>*/
def id1(x: Int): Int = x
def id2(x: Double): Double = x

def id[A](x: A) : A = x // a parametric expression
val eqInt = id[Int] _ // val eqInt: Int => Int = Lambda$6781/0x000002e128cd2c00@50fb7133
// Function types do not support polymorphism
// [A](A=>A) is not a valid function value type

id(3)
id("abc")

def applyn[A](f: A=> A, n: Int, x: A): A =
  n match{
    case 0 => x
    case _ => f(applyn(f, n-1, x))
  }

applyn((x:Int)=>x+1, 100, 3) // val res2: Int = 103
// (x: Int)=>x+1 is an anonymous function Int => Int
// to 3, add 1, 100 times
applyn((x: String)=>x+"!", 10, "gil") // val res3: String = gil!!!!!!!!!!
// to "gil", add "!", 10 times
applyn(id[String], 10, "hur") // val res4: String = hur
// to "hur", identity operation, 10 times

def foo[A, B](f: A=>A, x: (A,B)): (A,B)=
  (applyn[A](f, 10, x._1), x._2)

foo[String, Int] ((x: String)=>x+"!", ("abc", 10))
// val res5: (String, Int) = (abc!!!!!!!!!!,10)

//
// Parametric Polymorphism: Datatypes
//
sealed abstract class MyOption[A]
case class MyNone[A]() extends MyOption[A]
case class MySome[A](some: A) extends MyOption[A]

sealed abstract class MyList[A]
case class MyNil[A]() extends MyList[A]
case class MyCons[A](hd: A, tl: MyList[A]) extends MyList[A]

sealed abstract class BTree[A]
case class Leaf[A]() extends BTree[A]
case class Node[A](value: A, left: BTree[A], right: BTree[A]) extends BTree[A]

def x: MyList[Int] = MyCons(3, MyNil())
def y: MyList[String] = MyCons("abc", MyNil())

//
// Revisit: Solution with Tail Recursion
//

import annotation.tailrec
def find[A](t: BTree[A], x: A): Boolean=
  t match{
    case Leaf() => false
    case Node(n,_,_) if (n==x) => true
    case Node(_,l,r) => find(l, x) || find(r, x)
  }

def findRec[A](t: BTree[A], x: A): Boolean=
  @tailrec
  def findIter(ts: MyList[BTree[A]]): Boolean =
    ts match {
      case MyNil() => false
      case MyCons(Leaf(), tl) => findIter(tl)
      case MyCons(Node(v, _, _), _) if v == x => true // root has x
      case MyCons(Node(_, l, r), tl) =>
        findIter(MyCons(l, MyCons(r, tl))) // search from left first then right
    }
  findIter(MyCons(t, MyNil()))

//               rt
//        l               r
//   ll      lr      rl      rr
// lll llr lrl lrr rll rlr rrl rrr

//        l                r
//   ll      lr       rl      rr
// lll llr lrl lrr; rll rlr rrl rrr


//                           r
//   ll        lr       rl      rr
// lll llr; lrl lrr; rll rlr rrl rrr

//                           r
//             lr       rl      rr
// lll; llr; lrl lrr; rll rlr rrl rrr

//                                  r
//                    lr       rl      rr
// leaf; leaf; llr; lrl lrr; rll rlr rrl rrr

//                            r
//              lr       rl      rr
// leaf; llr; lrl lrr; rll rlr rrl rrr

//                       r
//         lr       rl      rr
// llr; lrl lrr; rll rlr rrl rrr

//                  r
//    lr       rl      rr
// lrl lrr; rll rlr rrl rrr

//                   r
//              rl      rr
// lrl; lrr; rll rlr rrl rrr

// ...

def genTree(v: Int, n: Int): BTree[Int]= // create tree of n nodes with value v
  @tailrec
  def genTreeIter(t: BTree[Int], m: Int): BTree[Int]=
    if (m == 0) t
    else genTreeIter(Node(v,t, Leaf()),m-1)
  genTreeIter(Leaf(), n)

//genTree(0,10000)
//           0
//         0
//       0
//     0
//   0
// 0
// ...

find(genTree(0, 10000), 1) // val res6: Boolean = false

//
// Exercise
//
sealed abstract class BSTree[A]
case class BSLeaf[A]() extends BSTree[A]
case class BSNode[A](key: Int, value: A, left: BSTree[A], right: BSTree[A]) extends BSTree[A]
def lookup[A](t: BSTree[A], key: Int): MyOption[A]=
  t match{
    case BSLeaf() => MyNone()
//    case BSNode(k, v, _, _) if k == key => MySome(v)
//    case BSNode(_, _, l, r) =>
//      lookup(l, key) match{
//        case MySome(v) => MySome(v)
//        case MyNone() => lookup(r, key)
//      }
// if BSTree does not satisfy BST property
    case BSNode(k, v, l, r) =>
      k match {
        case _ if k == key => MySome(v)
        case _ if k < key => lookup(r, key)
        case _ if k > key => lookup(l, key)
      }
  }

def t: BSTree[String] =
  BSNode(5, "My5",
    BSNode(4, "My4", BSNode(2, "My2", BSLeaf(), BSLeaf()), BSLeaf()),
    BSNode(7, "My7", BSNode(6, "My6", BSLeaf(), BSLeaf()), BSLeaf()))

lookup(t, 7) // val res7: MyOption[String] = MySome(My7)
lookup(t, 3) // val res8: MyOption[String] = MyNone()

// defining BSTree w/ BTree
type BSTree2[A] = BTree[(Int, A)]

def lookup2[A] (t: BSTree2[A], key: Int): MyOption[A] =
  t match{
    case Leaf() => MyNone()
    case Node((k, v), l, r) =>
      k match {
        case _ if k == key => MySome(v)
        case _ if k < key => lookup2(r, key)
        case _ => lookup2(l, key)
      }
  }

def t2: BSTree2[String] =
  Node((5,"My5"),
    Node((4,"My4"),Node((2,"My2"),Leaf(),Leaf()),Leaf()),
    Node((7,"My7"),Node((6,"My6"),Leaf(),Leaf()),Leaf()))

lookup2(t2, 7) // val res9: MyOption[String] = MySome(My7)
lookup2(t2, 3) // val res1-0: MyOption[String] = MyNone()

//
// Polymorphic Option (Library)
//

//Option[T] (library)
//introduction:
//  None
//  Some(x: T)
//elimination: pattern matching, library functions

Some(3): Option[Int]
Some("abc"): Option[String]
None: Option[Int] // unlike INone(), None does not need parenthesis... HOW?
None: Option[String]

//List[T] (library)
//introduction:
//  Nil
//  x::L
//elimination: pattern matching, library functions

"abc"::Nil : List[String]
List(1, 3, 4, 2, 5)
1::3::4::2::5::Nil : List[Int] // val res13: List[Int] = List(1, 3, 4, 2, 5)
/*</script>*/ /*<generated>*/
/*</generated>*/
}

object ParametricPolymorphism_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new ParametricPolymorphism$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export ParametricPolymorphism_sc.script as ParametricPolymorphism

