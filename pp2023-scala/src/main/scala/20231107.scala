// trait Iter[I,A]:
//     extension (i: I)
//         def getValue: Option[A]
//         def getNext: I
// trait Iterable[I,A]:
//     type Itr
//     given iterIF: Iter[Itr,A]
//     extension (i: I)
//         def iter: Itr
// // behaves like Iter[A] <: Iterable[A] in OOP
// given iter2iterable[I,A](using iterI: Iter[I,A]): Iterable[I,A] with
//     type Itr = I
//     def ItrI = iterI
//     extension (i:I)
//         def iter = i

// def sumElements[I](xs: I)(implicit II:Iterable[I,Int]) = {
//     def loop(i: II.Itr): Int =
//         i.getValue match {
//             case None => 0
//             case Some(n) => n + loop(i.getNext)
//         }
//     loop(xs.iter)
// }
// def printElements[I,A](xs: I)(implicit II: Iterable[I,A]) = {
//     def loop(i: II.Itr): Unit =
//         i.getValue match {
//             case None =>
//             case Some(a) => {println(a); loop(i.getNext)}
//         }
//     loop(xs.iter)
// }

// def testList[L](implicit ListI: Listlike[L,Int], ItrI: Iterable[L,Int]) = {
//     val l = (3 :: !()) ++ (1 :: 2 :: !())
//     println(sumElements(l)) //sumElements(l)(listIter[Int])
//     printElements(l) //printElements(l)(listIter[Int])
// }
// def testTree[T](implicit TreeI: Treelike[T,Int], ItrI: Iterable[T,Int]) = {
//     val t: T = 3.node(4.node(!(), !()), 2.node(!(),!()))
//     println(sumElements(t))
//     printElements(t)
// }

// // behaves like List[A] <: Iter[A] in OOP
// given listIter[A]: Iter[List[A],A] with
//     extension (l: List[A])
//         def getValue = l.headOption
//         def getNext = l.tail
// // behaves like List[A] <: Listlike[A] in OOP
// given listListlike[A] : Listlike[List[A],A] with
//     extension (u: Unit)
//         def unary_! = Nil
//     extension (a: A)
//         def ::(l: List[A]) = a::l
//     extension (l: List[A])
//         def head = l.headOption
//         def tail = l.tail
//         def :::(l2: List[A]) = l ::: l2

// enum MyTree[+A]:
//     case Leaf
//     case Node(value: A, left: MyTree[A], right: MyTree[A])
// import MyTree._

// // behaves like MyTree[A] <: Iterable[A], but clumsy in OOP
//     given treeIterable[L,A](using listI: Listlike[L,A], iterI: Iter[L,A])
//         : Iterable[MyTree[A], A] with
//         type Itr = L
//         def ItrI = iterI
//         extension (t: MyTree[A])
//         def iter: L = t match {
//         case Leaf => !()
//     case Node(v, lt, rt) => v :: lt.iter ::: rt.iter
// }


trait Stack[S,A]:
    extension (u: Unit)
        def empty : S // technically constructor
    extension (s: S)
        def get: (A,S) // top, tail(remaining)
        def put(a: A): S
def testStack[S](implicit StkI: Stack[S,Int]) = {
    val s0 = ().empty
    val s1 = s0.put(3)
    val s2 = s1.put(-2)
    val s3 = s2.put(4)
    val (v1,s4) = s3.get
    val (v2,s5) = s4.get
    (v1,v2)
}

given BasicStack[A] : Stack[List[A],A] with
    extension (u: Unit)
        def empty = List()
    extension (s: List[A])
        def get = (s.head, s.tail)
        def put(a: A) = a :: s

def StackOverridePut[S,A](newPut: (S,A)=>S)(implicit stkI: Stack[S,A]) =
    new Stack[S,A] {
        extension (u: Unit)
            def empty = stkI.empty(u)
        extension (s: S)
            def get = stkI.get(s)
            def put(a: A) = newPut(s,a)
    }
def Doubling[S](implicit stkI: Stack[S,Int]) : Stack[S,Int] =
    StackOverridePut((s,a) => s.put(2 * a))
def Incrementing[S](implicit stkI: Stack[S,Int]) : Stack[S,Int] =
    StackOverridePut((s,a) => s.put(a + 1))
def Filtering[S](implicit stkI: Stack[S,Int]) : Stack[S,Int] =
    StackOverridePut((s,a) => if (a >= 0) s.put(a) else s)


def printAStack(stk: Stack[S, A]): Unit=
    

@main def test: Unit=
    // testStack(BasicStack)
    testStack
    // testStack(Filter
    ing(Incrementing (Doubling(BasicStack))))
    testStack(Filtering(Incrementing (Doubling)))
    // testStack(Filtering(Incrementing(Incrementing(Doubling(BasicStack)))))
    testStack(Filtering(Incrementing(Incrementing(Doubling))))
