import scalaz._, Scalaz._

package object sandbox {

  def f[A, B](xs: List[Either[List[A], B]]): List[Either[A, B]] =
    xs.flatMap(_.fold(_.map(Left(_)), b => List(Right(b))))

  def g[A, B](xs: IList[IList[A] \/ B]): IList[A \/ B] =
    xs.flatMap(_.bitraverse(identity, IList(_)))

}
