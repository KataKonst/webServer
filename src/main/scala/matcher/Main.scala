package matcher


import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

object Main extends App {
    implicit val system = ActorSystem("match-service")

    val service = system.actorOf(Props[MatcherActor], "match-service")

    val host = "192.168.1.105";
     val port = 8080
    IO(Http) ! Http.Bind(service, host, port = port)

}
