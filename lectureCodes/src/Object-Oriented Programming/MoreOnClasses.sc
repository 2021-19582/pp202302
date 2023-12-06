// Motivating Example
class Primes(val prime: Int, val primes: List[Int]) {
  def getNext: Primes = {
    val p = computeNextPrime(prime + 2)
    new Primes(p, primes ++ (p :: Nil))
  }
  def computeNextPrime(n: Int): Int =
    if (primes.forall((p: Int)=> n%p != 0)) n
    else computeNextPrime(n+2)
}

def nthPrime(n: Int): Int =
  def go(primes: Primes, k: Int): Int =
    if (k <= 1) primes.prime
    else go(primes.getNext, k-1)

  if (n <= 0) 2 else go(new Primes(3, List(3)), n)

nthPrime(10000) // val res0: Int = 104743

//
// Access Modifiers
//
// private/protected
// private: only class
// private: class + subclasses
class Primes2 private(val prime: Int, protected val primes: List[Int])
{
  def this() = this (3, List(3))
  def getNext: Primes2 = {
    val p = computeNextPrime(prime + 2)
    new Primes2(p, primes ++ (p :: Nil))
  }
  private def computeNextPrime(n: Int): Int =
    if (primes.forall((p: Int)=> n%p != 0)) n
    else computeNextPrime(n+2)
}

def nthPrime2(n: Int): Int = {
  def go(primes: Primes2, k: Int): Int =
    if (k <= 1) primes.prime
    else go(primes.getNext, k-1)

  if (n==0) 2 else go (new Primes2, n)
}

nthPrime2(10000) // val res1: Int = 104743