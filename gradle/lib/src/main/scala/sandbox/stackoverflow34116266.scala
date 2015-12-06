import scala.util.{Success, Failure, Try}

package object stackoverflow34116266 {

  implicit class OptionTry[A](oa: Option[A]) extends AnyVal {

    def asTry[E <: Exception](e: E): Try[A] = oa match {
      case Some(a) => Success(a)
      case None    => Failure(e)
    }

    def asTry(msg: String): Try[A] = asTry(new Exception(msg))
  }

  // ---

  def foo0[A](o: Option[A], e: Exception) =
    o.asTry(e)

  def foo1[A](o: Option[A], e: Exception) =
    Try(o.getOrElse(throw e))

  def foo2[A](o: Option[A], e: Exception) =
    o.fold[Try[A]](Failure(e))(Success(_))

  // ---

  def bar0[A](o: Option[A], s: String) =
    o.asTry(s)

  def bar1[A](o: Option[A], s: String) =
    o.fold[Try[A]](Failure(new Exception(s)))(Success(_))

  def bar2[A](o: Option[A], s: String) =
    Try(o.getOrElse(throw new Exception(s)))

  // ---

  def baz[A](o: Option[A], s: String) =
    o.toRight(s)

}
