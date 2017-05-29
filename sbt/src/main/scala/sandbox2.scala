object typeclasses {

  sealed trait Things[A]
  case class Cons[A](head: A, tail: Things[A]) extends Things[A]
  case class Nope[A]() extends Things[A]



}
