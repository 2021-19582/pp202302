id: file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main.scala:[6501..6506) in Input.VirtualFile("file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main.scala", "
// // @main def hello: Unit =
// //   println("Hello world!")
// //   println(msg)

// // def msg = "I was compiled by Scala 3. :)"


// /**************************2023-10-17**************************/
// class MyTree[A](v: A,
//   lt: Option[MyTree[A]],
//   rt: Option[MyTree[A]]) 
// {
//   val value = v   
//   val left = lt
//   val right = rt
// }   

// type YourTree[A] = Option[MyTree[A]]
// object YourTree // companion obj
// {
//   def apply[A](v:A, lt:Option[MyTree[A]], rt:Option[MyTree[A]]) =
//   Some(new MyTree(v,lt,rt))
// }

// val t0 : YourTree[Int] = None
// val t1 : YourTree[Int] = Some(new MyTree(3, None, None))
// val t2 : YourTree[Int] = Some(new MyTree(3, Some (new MyTree(4,None,None)), None))

// // Nominal sub type
// // class foo_type(x: Int, y: Int) {
// //   val a : Int = x
// //   def b : Int = a + y
// //   def f(z: Int) : Int = b + y + z
// // }
// // class gee_type(x: Int) extends foo_type(x+1,x+2) { 
// //   // g is subtype of foo (nominal sub typing)
// //   // gee_type <: foo_type
// //   // g type default has all the fields of foo
// //   val c : Int = f(x) + b // g has a, b, f, c fields
// // }

// // def test_1: Unit={
// //   (new gee_type(30)).c
// //   def test(f: foo_type) = f.a + f.b
// //   println(test(new foo_type(10,20)))
// //   println(test(new gee_type(30)))
// // }

// // overriding 1
// class foo_type(x: Int, y: Int) {
// val a : Int = x
// def b : Int = 0
// def f(z: Int) : Int = b * z
// }
// class gee_type(x: Int) extends foo_type(x+1,x+2) {
// override def b = 10 
// }

// @main def test_3={
//   println((new gee_type(30)).f(10))
//   println((new foo_type(31, 32).f(10)))
// }

// // changes the intention from the parent
// // you would not want to use overriding to much- decreases readability
// // since while f is only in def (and not evaluated)
// // when f is overridden then 

// // overload looks similar but the two functions are completely unrelated
// // only the name (the alias) is *coincidently* identical
// // we cannot override with a diff type- THAT IS OVERLOADING and the og ftn is unrelated to the new ftn

// class MyList[A](v: A, nxt: Option[MyList[A]]) {
//   val value : A = v
//   val next : Option[MyList[A]] = nxt
// }
// type YourList[A] = Option[MyList[A]]
// val t : YourList[Int] = 
//   Some(new MyList(3,Some (new MyList(4,None))))
// // we used Option type to make MyList
// // this is an algebraic type

// // we can use inheritance to do this-
// // MyNil <: MyList
// // MyCons <: MyList
// class MyList[A]()

// class MyNil[A]() extends MyList[A]
// object MyNil {def apply[A]() = new MyNil[A]} // the companion obj- we don't need to use new no more
// class MyCons[A](val hd: A, val tl: MyList[A]) 
//   extends MyList[A]
// val t: MyList[Int] = 
//   new MyCons(3, new MyCons(4, new MyNil()))

// // BUT WE DON'T HAVE ANY ELIMINATION
// // for instance, how will you compute the length of a MyList?

// // Nothing is a subtype of anytype
// // covariant +A MyTree[+A]: A <: B => MyTree[A] <: MyTree[B]
// // contravariant -A MyTree[-A]: A <: B => MyTree[B] <: MyTree[A]

// sealed abstract class MyOption[A] {
// def matches[B](e1 : =>B, e2: A=>B) : B
// }
// class MyNone[A]() extends MyOption[A] {
// def matches[B](e1 : =>B, e2: A=>B) = e1
// }
// object MyNone { def apply[A]() = new MyNone() }
// class MySome[A](v: A) {
// def matches[B](e1 : =>B, e2: A=>B) = e2(v)
// }
// object MySome { def apply[A](v: A) = new MySome(v) }

// @main def test_4: Unit={
// val x = MySome(42)
// val y : MyOption[Int] = MyNone()
// x.matches(0,(n)=>n+1)
// y.matches(0,(n)=>n+1)}


// Abstract class

// abstract class: interfacce
// we cannot learn all tho code in a library
// the interface allows us to use lib. w/o needing to fully understand the source code
// we cannot make an instance out of abstract class

// class Primes(val prime: Int, val primes: List[Int]){
//     def getNext: Primes = {
//         val p = computeNextPrime(prime +2)
//         new Primes(p, primes ++ (p :: Nil))
//     }
//     def computeNextPrime(n: Int): Int = {
//         if (primes.forall((p: Int)=>n%p !=0)) n
//         else computeNextPrime(n+2)
//     }
// }

// def nthPrime(n: Int): Int ={
//     def go(primes: Primes, k: Int): Int ={
//         if (k <= 1) primes.prime
//         else go(priems.getNext, k-1)
//     }
//     if (n<=0) 2 else go(new Primes(3, List(3)), n)
// }

// nthPrime(10000)


class Primes(val prime: Int, val primes: List[Int]){
    def this() = this(3, List(3))
    def getNext: Primes ={
        val p = computeNextPrime(prime+2)
        new Primes(p, primes ++ (p :: Nil))
    }
    def computeNextPrime(n: Int): Int = {
        if (primes.forall((p:Int) => n%p != 0)) n
        else computeNextPrime(n+2)

    }
}

// OOP 의 interface는 elimination밖에 안됨. introduction이 안됨!
// interface로 
// 하지만 type class는 잘 된다.

// access modifier 
// protected: 나와 내 자식만이 쓸 수 있는
// 사실 typeclass의 interface이 훨씬 우아하다

class Primes private (val prime: Int, protected val primes: List[Int])
{
    def this() = this (3, List(3))
    def getNext: Primes = {
        val p = computeNextPrime(prime + 2)
        new Primes(p, primes ++ (p :: Nil))
    }
    private def computeNextPrime(n: Int) : Int =
        if (primes.forall((p:Int) => n%p != 0)) n
        else computeNextPrime(n+2)
}

def nthPrime(n: Int): Int = {
    def go(primes: Primes, k: Int): Int =
        if (k <= 1) primes.prime
        else go(primes.getNext, k - 1) 
    if (n == 0) 2 else go(new Primes, n)
}
nthPrime(10000)

// DIAMOND PROBLEM
// C++: 니가 알아서 근친 피해라... 모든 건 니 책임, 나는 그냥 A(10) 부르고 A(20) 부를거임 뺴액
// Java: multiple inheritace 안받을거임! 단, interface라고 완전히 abstact 한 class에 대해서만 여러 부모를 가질 수 있게 해줄게
//   (다만 추상화의 목적만 있고 코드 재활용이 안됨- elimination만 가능하고 intro가 없음)
// Scala: 나는 여러 부모를 받고 싶어서 trait라는 것을 받을거야! (rust의 trati와는 또 다른)
//   Trait는 constructor이 인자를 받지 않는 1개인 경우의 abstract class
//   삼촌 라인-!
//   trait의 부모로 class가 올 수 있으나, argument가 오면 안됨
//   또한 그 class는 내 원래 부모 라인에 있으야 해
//   메인 가계도에는 cycle이 있을 수는 없다.


class A(val a: Int){
    def this() = this(0)
}

trait B{
    def f (x:Int)
}

trait C extends A with B{
    def g(x: Int)
}


trait D extends B{
    def g(x: Int): Int
}




abstract class Iter[A]{
    def getValue: Option[A]
    def getNext: Iter[A]
}

class 

class ListIterDict[K,V]
(eq: (K,K)=> Boolean, lsit: List )
extends ListIter[(K,V)](list) with Dict[K, V]{

}")
file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main.scala
file:///C:/Users/amych/github_SNU/2023-2/pp202302/pp2023-scala/src/main/scala/Main.scala:231: error: expected identifier; obtained class
class ListIterDict[K,V]
^
#### Short summary: 

expected identifier; obtained class