import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.util.Try

package object sandbox {

  class Foo extends Actor {

    import context.dispatcher

    override def preStart(): Unit = println("actor start")

    override def receive: Receive = {
      case x =>
        pipe(Future(throw new RuntimeException("hi"))).to(sender)
    }

    override def postStop(): Unit = println("actor stop")
  }

  def main(args: Array[String]): Unit = {
    println("system start")
    val system = ActorSystem()
    val foo = system.actorOf(Props[Foo])
    implicit val timeout: Timeout = 1.second
    println(Try(Await.result(foo.ask('hi), 1.second)))
    io.StdIn.readLine()
    Thread.sleep(2.seconds.toMillis)
    println("terminating")
    Await.result(system.terminate(), 1.second)
  }

}
