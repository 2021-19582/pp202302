import scala.language.higherKinds
//trait Iter[I,A] {
// def getValue(a: I): Option[A]
// deI getNext(a: I): I }
trait Iter[I[_]] { // means that I gets a type... you can elabor
    def getValue[A](a: I[A]) : Option[A]
    def getNext[A](a: I[A]) : I[A]
}
//trait Iterable[R,A] {
// type Itr
// def iterIF: Iter[Itr, A]
// def iter(a: R): Itr
//}
trait Iterable[R[_]] {
    type Itr[_]
    def iter[A](a: R[A]): Itr[A]
    def iterIF: Iter[Itr]
}