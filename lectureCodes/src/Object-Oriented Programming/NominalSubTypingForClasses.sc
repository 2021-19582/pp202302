import reflect.Selectable.reflectiveSelectable

//
// Nominal Sub Typing, a.k.a. Inheritance
class foo_type(x: Int, y: Int){
  val a: Int = x
  def b: Int = a + y
  def f(z: Int): Int = b + y + z
}

class gee_type(x: Int) extends foo_type(x+1, x+2) { // nominally defining gee_type <: foo_type
  val c: Int = f(x) + b
}

(new gee_type(30)).c

def test(f: foo_type) = f.a + f.b
test(new foo_type(10, 20))
test(new gee_type(30))

//
// Overriding
//
class foo_type(x: Int, y: Int) {
  val a: Int = x
  def b: Int = 0
  def f(z: Int): Int = b * z
}