
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
