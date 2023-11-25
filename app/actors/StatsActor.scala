package actors

import akka.actor.Actor

class StatsActor extends Actor {
  private var counter = 0
  override def receive: Receive = {
    case StatsActor.Ping => ()
    case StatsActor.RequestReceived => counter += 1
    case StatsActor.GetStats => sender() ! counter  // ! means tell and is fire and forget
  }
}

object StatsActor {
  val name = "statsActor"
  val path = s"/user/$name"

  case object Ping
  case object RequestReceived
  case object GetStats
}
