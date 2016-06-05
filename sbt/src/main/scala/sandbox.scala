import java.util.concurrent.TimeUnit

import sandbox.Handle.Listener

import scala.concurrent.Future

package object sandbox {

  def unblock[A](x: => Handle[A]): Handle[A] =
    new Handle[A] {

      private[this] val lock = new Object()

      val listeners = collection.mutable.ArrayBuffer[Listener[A]]()
      var handleOption: Option[Handle[A]] = None

      Future(x).onSuccess {
        case h => lock.synchronized {
          handleOption = Some(h)
          listeners.foreach(h.addListener)
        }
      }

      override def addListener(l: Listener[A]): Unit = lock.synchronized {
        handleOption.fold(listeners.append(l))(_.addListener(l))
      }

      override def isCancelled: Boolean = lock.synchronized {
        handleOption.fold(listeners.append(l))(_.addListener(l))
      }

      override def get(): A = ???
      override def get(timeout: Long, unit: TimeUnit): A = ???
      override def cancel(mayInterruptIfRunning: Boolean): Boolean = ???
      override def isDone: Boolean = ???
    }

}
