package controllers

import actors.StatsActor
import controllers.Assets.Asset

import java.util.concurrent.TimeUnit
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask

// import javax.inject._ - no longer needed a not using Guice
import play.api.mvc._
import services.{SunService, WeatherService}

import scala.concurrent.ExecutionContext.Implicits.global
// import play.api.libs.ws.WSClient - no longer needed as injected in AppApplicationLoader


class Application (components: ControllerComponents, assets: Assets,
                   // Pass all dependencies as constructor parameters
                   sunService: SunService, weatherService: WeatherService, actorSystem: ActorSystem) // pass ActorSystem
    extends AbstractController(components) {

  def index: Action[AnyContent] = Action.async {
    val lat = -33.8830
    val lon = 151.2167
    val date = new java.util.Date()
    val dateTimeStr = new java.text.SimpleDateFormat("dd-mm-yyyy hh:mm:ss").format(date)

    for {
      sunInfo <- sunService.getSunInfo(lat, lon)
      temperature <- weatherService.getTemperature(lat, lon)
      noRequest <- {
        implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)
        (actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetStats).mapTo[Int] // ? means Ask and returns a future
      }
    } yield {
      Ok(views.html.index(dateTimeStr, sunInfo, temperature, noRequest))
    }
  //  Future.successful { Ok(views.html.index(dateTimeStr, SunInfo("Rise", "Set", 99.9))) }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}

