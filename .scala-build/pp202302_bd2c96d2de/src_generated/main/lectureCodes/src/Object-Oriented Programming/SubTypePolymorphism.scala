
package lectureCodes.src.Object$minusOriented$u0020Programming



final class SubTypePolymorphism$_ {
def args = SubTypePolymorphism_sc.args$
def scriptPath = """lectureCodes/src/Object-Oriented Programming/SubTypePolymorphism.sc"""
/*<script>*/
//
// Motivation
//
object tom{
  val name = "Tom"
  val home = "02-880-1234"
} // tom: {val name: String; val home: String}
object bob{
  val name = "Bob"
  val mobile = "010-1111-2222"
} // tom: {val name: String; val home: String}
def greeting(r: ???) = "Hi " + r.name + ", How are you?"
greeting(tom)
greeting(bob)
// can we have compiler to get name of object and use, regardless of specific type?

import reflect.Selectable.reflectiveSelectable

type NameHome = {
  val name: String;
  val home: String
}

type NameMobile = {
  val name: String;
  val mobile: String
}

type Name = {
  val name: String
//  val dummy: String // prohibits subtype relation
}

//compiler implicitly assumes
//NameHome <: Name // NameHome is subtype of Name
//NameMobile <: Name // NameMobile is subtype of Name

def greeting(r: Name) = "Hi " + r.name + ", How are you?"
greeting(tom) // tom: NameHome
greeting(bob) // bob: NameMobile
// runs w/o declaration of NameHome and NameMobile

//
// Sub Types
//

//T <: S // every element of T *can be used as* that of S
//not necessarily subset relation (= every element of T is that of S)

//T<:S
//then S=>R <: T=>R
//also R=>T <: R=>S (which is obvious)

//
// Two kinds of sub types
//

// structural sub type (Duck typing)
// system implicitly determines sub type relation
// based on data structure
// if structurally equiv (including variable names), then considered same

// nominal sub type (ad hoc Polymorphism)
// explicitly specifies sub type relation
// structurally equiv types w/ different names are treated diffrently
/*</script>*/ /*<generated>*/
/*</generated>*/
}

object SubTypePolymorphism_sc {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }

  lazy val script = new SubTypePolymorphism$_

  def main(args: Array[String]): Unit = {
    args$set(args)
    script.hashCode() // hashCode to clear scalac warning about pure expression in statement position
  }
}

export SubTypePolymorphism_sc.script as SubTypePolymorphism

