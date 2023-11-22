import reflect.Selectable.reflectiveSelectable

type gee_type = {val name: String; val age: Int; def getPP(): String}
def gee_fun(_name: String, _age: Int): gee_type = {
  if (!(_age >= 0 && _age < 200)) throw new Exception("Out of range")
  object tmp {
    val name: String = _name
    val age: Int = _age

    def getPP(): String = name + " of age " + age.toString()
  }
  tmp
}

val gee: gee_type = gee_fun("David Jones", 25)

gee.getPP() // val res0: String = David Jones of age 25