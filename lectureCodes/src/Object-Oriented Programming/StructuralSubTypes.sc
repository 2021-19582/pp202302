import reflect.Selectable.reflectiveSelectable
// required to select wrt name of type attribute

//
// General sub type rules
//

// Reflexivity: T<:T
// Transitivity: T:<R, R<:S => T<:S

// special types
// Nothing<:T<:Any
val a: Int = 3
val b: Any = a // int is any but any is not int
def f(a: Nothing): Int = a // nothing is int

//{val x: { val y: Int; val z: String}, val w: Int}
//<: // by permutation
//{val w: Int; val x: { val y: Int; val z: String}}
//<: // by depth and width (x prev <: x next)
//{val w: Int; val x: {val z: String}}

//
// sub type for functions
//
def foo(s: {val a: Int; val b: Int}): {val x: Int; val y: Int} = {
  object tmp{
    val x = s.b
    val y = s.a
  }
  tmp
}
val gee: {val a: Int; val b: Int; val c: Int} => {val x: Int} = foo _
// val gee: Object{val a: Int; val b: Int; val c: Int} => Object{val x: Int} = Lambda$7454/0x000002e1299c4000@595d5592