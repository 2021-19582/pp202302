// IntStack Spec
trait Stack[S, A]:
  extension (u: Unit)
    def empty: S
  extension (s: S)
    def get: (A, S)
    def put(a: A): S

def testStack[S](implicit stkl: Stack[S, Int]) =
  val s0 = ().empty
  val s1 = s0.put(3)
  val s2 = s1.put(-2)
  val s3 = s2.put(4)
  val (v1, s4) = s3.get
  val (v2, s5) = s4.get
  (v1, v2)

// Implementation using List
given BasicStack[A]: Stack[List[A], A] with
  extension (u: Unit)
    def empty = List()

  extension (s: List[A])
    def get = (s.head, s.tail)
    def put(a: A) = a +: s

// Modifying traits
def StackOverridePut[S, A](newPut: (S, A)=> S)(implicit stkl: Stack[S, A]) =
  new Stack[S, A]{
    extension (u: Unit)
      def empty = stkl.empty(u)

    extension (s: S)
      def get = stkl.get(s)
      def put(a: A) = newPut(s, a)
  }

def Doubling[S](implicit stkl: Stack[S, Int]): Stack[S, Int] =
  StackOverridePut((s, a)=> s.put(2*a))

def Incrementing[S](implicit  stkl: Stack[S, Int]): Stack[S, Int] =
  StackOverridePut((s, a)=> s.put(a+1))

def Filtering[S](implicit stkl: Stack[S, Int]): Stack[S, Int] =
  StackOverridePut((s, a)=>{if a>=0 then s.put(a) else s})

testStack // testStack(BasicStack)
// val res0: (Int, Int) = (4,-2)
testStack(Incrementing(BasicStack))
// val res1: (Int, Int) = (5,-1)
testStack(Filtering(Incrementing(Doubling))) // testStack(Filtering(Incrementing(Doubling(BasicStack)))
// val res3: (Int, Int) = (10,8)
testStack(Incrementing(Doubling))
// val res3: (Int, Int) = (10,-2) // B(I(D)))

// Implementation: Sorted Stack
def SortedStack: Stack[List[Int], Int] = new {
  extension (u: Unit)
    def empty: List[Int] = List()

  extension (s: List[Int])
    def get = (s.head, s.tail)
    def put(a: Int) : List[Int] = {
      def loop (l: List[Int]): List[Int] =
        l match{
          case Nil => a::Nil
          case hd::tl => if (a<=hd) a::l else hd::loop(tl)
        }
      loop(s)
    }
}

testStack(Filtering(Incrementing(Doubling(SortedStack)))) // 8, 10

// A trick for reducing boiler plate code

//def StackOverridePut[S, A] (newPut: (S, A)=>S)(implicit stkl: Stack[S, A])=
//  new Stack[S, A] {
//    extension(u: Unit)
//      def empty = stkl.empty(u)
//
//    extension(s: S)
//      def get = stkl.get(s)
//      def put(a: A) = newPut(s,a)
//  }
class mkStack[S, A](stkl: Stack[S, A]) extends Stack[S, A]:
  extension(u: Unit)
    def empty = stkl.empty(u)

  extension(s: S)
    def get = stkl.get(s)
    def put(a: A) = stkl.put(s)(a)

  def StakcOverridePut[S, A](newPut: (S, A)=>S)(implicit stkl: Stack[S, A])=
    new mkStack[S, A](stkl) {
      extension (s: S)
        override def put(a: A) = newPut(s, a)
    }