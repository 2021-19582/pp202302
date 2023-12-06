
package lectureCodes.src



final class TypeChecking$ampInference$_ {
def args = TypeChecking$ampInference_sc.args$
def scriptPath = """lectureCodes/src/TypeChecking&Inference.sc"""
/*<script>*/
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
/*</script>*/ /*<generated>*/
/*</generated>*/
}

object TypeChecking$ampInference_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new TypeChecking$ampInference$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export TypeChecking$ampInference_sc.script as TypeChecking$ampInference

