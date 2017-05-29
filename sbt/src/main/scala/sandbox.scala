package object things_definition {

  /*
    First we'll just look at what a simple cons list looks like in Scala.

    This is akin to

        Things a = Cons a (Things a) | Nope
  */

  sealed trait Things[A]
  case class Cons[A](head: A, tail: Things[A]) extends Things[A]
  case class Nope[A]() extends Things[A]

}

package object semigroup_definition {

  /*
    Consider how we might declare an interface for semigroups.

        class Semigroup a where
            mappend :: a -> a -> a

    Let's take an "is-a" approach, where a type that is a semigroup extends
    the `Semigroup` interface to declare that it is a semigroup. This is what
    people with an OO background tend to first think when their intuition says
    "typeclasses are like interfaces".
  */

  trait Semigroup[F] { self: F =>
    def mappend(that: F): F
  }

  /*
    Notice the three `F`s in the type above; they correspond to the three `a`s
    in the `mappend` signature. The first argument to `mappend` is the value
    the method is being invoked on.

    So a call to this equivalent to Haskell

        x `mappend` y

    will look like

        x.mappend(y)
  */

}

package object things_implementing_semigroup {

  /*
    Now we'll put them together - making Things implements the Semigroup
    interface.
  */

  import semigroup_definition._

  sealed trait Things[A] extends Semigroup[Things[A]]

  case class Cons[A](head: A, tail: Things[A]) extends Things[A] {
    def mappend(that: Things[A]): Things[A] = Cons(head, tail.mappend(that))
  }

  case class Nope[A]() extends Things[A] with Semigroup[Things[A]] {
    def mappend(that: Things[A]): Things[A] = that
  }

}

package object things_implementing_monoid_attempt {

  /*
    Let's try to make an interface for monoid.

    Remember this is

        class Monoid a where
            mappend :: a -> a -> a
            mempty :: a

    Using the same approach, we might try something like this:
  */

  trait Monoid[F] { self: F =>
    def mappend(that: F): F
    def mempty: F
  }

  /*
    But that's actually equivalent to

        class Semigroup a where
            mappend :: a -> a -> a
            mempty :: a -> a

    because we always have a first "this" argument. Therefore, using this
    approach our typeclass can only have members of types `a -> ...`, so
    this attempt to represent typeclasses as interfaces fails.
  */

}

package object things_has_a_monoid {

  /*
    Instead we can take the "has-a" approach; rather than having our Things
    class extending the interface ("Things[A] is a Monoid"), we'll declare
    the typeclass instance separately ("Things[A] has a Monoid").
  */

  trait Monoid[F] {
    def mappend(left: F, right: F): F
    def mempty: F
  }

  import things_definition._

  def thingsMonoidInstance[A]: Monoid[Things[A]] =
    new Monoid[Things[A]] {

      def mappend(left: Things[A], right: Things[A]): Things[A] =
        left match {
          case Cons(head, tail) => Cons(head, mappend(tail, right))
          case Nope() => right
        }

      def mempty: Things[A] = Nope()
    }

}

package object doubling {

  /*
    Let's try to use this to write a function that "doubles" something by
    mappending it to itself.
  */

  import things_has_a_monoid._

  def double[F](x: F, m: Monoid[F]): F = m.mappend(x, x)

  /*
    It's a bit obnoxious that we have to pass the monoid instance along
    explicitly. For instance, a call to this looks like

        double(Cons(1, Nope), thingsMonoidInstance)
  */

}

package object using_implicits {

  /*
    We can fix this, though, because Scala supports passing parameters
    implicitly. Once we do this, it starts to look a lot like typeclasses.
  */

  import things_definition._
  import things_has_a_monoid.Monoid

  implicit def thingsMonoidInstance[A]: Monoid[Things[A]] =
    things_has_a_monoid.thingsMonoidInstance

  def double[F](x: F)(implicit m: Monoid[F]): F = m.mappend(x, x)

  /*
    Now we can call this as

        double(Cons(1, Nope))

    The implicit parameter

        (implicit m: Monoid[F])

    serves the same role as a typeclass constraint

        (Monoid F) =>

    The result is that the monoid instance is just looked up automatically by
    type, as it would be in Haskell.
  */
}
