// sealed abstract class IList[T]
// case class INil[T]() extends IList[T] // similar but more technical implementation of null
// case class ICons[T](hd: T, tl: IList[T]) extends IList[T] // you cannot extend multiple classes


// def reverseStack[A](myList: IList[A], myRev: IList[A]): IList[A]={
//     myList match{
//         case INil() => myRev
//         case ICons(hd, tl) => reverseStack(tl, ICons(hd, myRev))
//     }
//   }

// def printStack[A](myList: IList[A]):Unit ={
//     myList match{
//         case INil() => println("INil")
//         case ICons(hd, tl) => {println(hd); printStack(tl)}
//     }
// }



// @main def test: Unit={
//     def myStack = ICons(1, ICons(2, ICons(3, INil())))
//     printStack(myStack)
//     printStack(reverseStack(myStack, INil()))
// }