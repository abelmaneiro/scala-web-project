package filters

import actors.StatsActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future

class StatsFilter (actorSystem: ActorSystem, // DI ActorSystem
                    implicit val mat: Materializer) extends Filter {
  private val log = Logger(this.getClass)
  override def apply(nextFilter: RequestHeader => Future[Result])(header: RequestHeader): Future[Result] = {
    log.info(s"Serving another request: ${header.path}")
    actorSystem.actorSelection(StatsActor.path) ! StatsActor.RequestReceived  // send message to actor
    nextFilter(header)
  }
}
