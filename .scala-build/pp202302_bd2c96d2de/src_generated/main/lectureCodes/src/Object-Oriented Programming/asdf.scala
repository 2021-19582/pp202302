
package lectureCodes.src.Object$minusOriented$u0020Programming



final class asdf$_ {
def args = asdf_sc.args$
def scriptPath = """lectureCodes/src/Object-Oriented Programming/asdf.sc"""
/*<script>*/
{
  abstract class Iterable[A] {
    def iter : Iter[A]
  }

  abstract class Iter[A] extends Iterable[A] {
    def getValue: Option[A]
    def getNext: Iter[A]
    def iter = this
  }
}
/*</script>*/ /*<generated>*/
/*</generated>*/
}

object asdf_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new asdf$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export asdf_sc.script as asdf

