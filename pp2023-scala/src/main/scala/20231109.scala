trait Listlike[L,A]:
    extension(u:Unit)
        def unary_! : L
    extension(elem:A)
        def ::(l: L): L
    extension(l: L)
        def head: Option[A]
        def tail: L
        def ++(l2: L): L
trait Treelike[T,A]:
    extension(u:Unit)
        def unary_! : T
    extension(a:A)
        def node(lt: T, rt: T): T
    extension(t: T)
        def root : Option[A]
        def left : T
        def right : T

def testList[L](implicit ListI: Listlike[L,Int], ItrI: Iterable[L,Int]) = {
val l = (3 :: !()) ++ (1 :: 2 :: !())
println(sumElements(l)) //sumElements(l)(listIter[Int])
printElements(l) //printElements(l)(listIter[Int])
}
def testTree[T](implicit TreeI: Treelike[T,Int], ItrI: Iterable[T,Int]) = {
val t: T = 3.node(4.node(!(), !()), 2.node(!(),!()))
println(sumElements(t))
printElements(t)
}

// behaves like List[A] <: Iter[A] in OOP
given listIter[A]: Iter[List[A],A] with
extension (l: List[A])
def getValue = l.headOption
def getNext = l.tail
// behaves like List[A] <: Listlike[A] in OOP
given listListlike[A] : Listlike[List[A],A] with
extension (u: Unit)
def unary_! = Nil
extension (a: A)
def ::(l: List[A]) = a::l
extension (l: List[A])
def head = l.headOption
def tail = l.tail
def :::(l2: List[A]) = l ::: l2
