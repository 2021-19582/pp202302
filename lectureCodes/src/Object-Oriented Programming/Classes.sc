import reflect.Selectable.reflectiveSelectable

//
// Class: Parameterized Record
//
type gee_type = {val name: String; val age: Int; def getPP(): String}
def gee_fun(_name: String, _age: Int): gee_type = {
  if (!(_age >= 0 && _age < 200)) throw new Exception("Out of range")
  object tmp {
    val name: String = _name
    val age: Int = _age

    def getPP(): String = name + " of age " + age.toString()
  }
  tmp
}

val gee: gee_type = gee_fun("David Jones", 25)

gee.getPP() // val res0: String = David Jones of age 25

class foo_type(_name: String, _age: Int) {
  if (!(_age >= 0 && _age < 200)) throw new Exception ("Out of range")
  val name: String = _name
  val age: Int = _age
  def getPP(): String = name + " of age " + age.toString()
}
val foo: foo_type = new foo_type("David Jones", 25)
// val foo: foo_type = foo_type@70c0083e

val v1: gee_type = foo
//val v2: foo_type = gee // [E007] Type Mismatch Error

def greeting(r:{val name: String}) =
  "Hi " + r.name + ", How are you?"

greeting(foo)

//
// Class: Can be Recursive
//
class MyList[A] (v: A, nxt: Option[MyList[A]]) {
  val value: A = v
  val next: Option[MyList[A]] = nxt
}
type YourList[A] = Option[MyList[A]] // renaming


val t: YourList[Int] =
  Some(new MyList(3, Some(new MyList(4, None)))) // val t: YourList[Int] = Some(rs$line$11$MyList@3391a772)
val t2: Option[MyList[Int]] =
  Some(new MyList(3, Some(new MyList(4, None))))
// it's discouraged to use null in Scala although Scala supports null for
// compatibility with Java.

//
// Simplification using Argument Members
//
class MyList2[A](val value:A, val next:Option[MyList[A]]) {}
class MyList3[A](val value:A, val next:Option[MyList[A]])

//
// Simplification using Companion Object
//
object MyList{ // companion obj of MyList class
  def apply[A] (v: A, nxt: Option[MyList[A]]) =
    new MyList(v, nxt)
}

object YourList{ // NOT companion obj of YourList but anyways...
  def apply[A] (v: A, nxt: Option[MyList[A]]) =
    Some(new MyList(v, nxt))
}

val t10 = None
val t11 = Some(new MyList(3, Some(MyList(4, None))))
// val t11: Some[MyList[Int]] = Some(rs$line$11$MyList@17e02781)
val t12 = YourList(3, (YourList (4, None)))
// val t12: Some[MyList[Int]] = Some(rs$line$11$MyList@3f0d88f1)

//
// Exercise
//
class MyTree[A](val value: A, val left: Option[MyTree[A]], val right: Option[MyTree[A]])
type YourTree[A] = Option[MyTree[A]]

val t20: YourTree[Int] = None
val t21: YourTree[Int] = Some(new MyTree(3, None, None))
val t22: YourTree[Int] =
  Some(new MyTree(3, Some(new MyTree (4, None, None)), None))

object YourTree{
  def apply[A] (v:A, left: Option[MyTree[A]], right: Option[MyTree[A]]) =
    Some(new MyTree(v, left, right))
}

val t30: YourTree[Int] = None
val t31: YourTree[Int] = YourTree(3,None,None)
val t32: YourTree[Int] = YourTree(3,YourTree(4,None,None),None)