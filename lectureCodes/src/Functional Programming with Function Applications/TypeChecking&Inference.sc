//
// Types
//

// typed programming
def id1(x: Int): Int = x
def id2(x: Double): Double = x

// untyped programming
//def id(x) = x // does not work just yet...

// type inference: allows compiler to assume type
def f(x: Int) = x > 3
// too much type inference is bad in terms of readability