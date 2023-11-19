package controllers

import controllers.Assets.Asset

// import javax.inject._ - no longer needed a not using Guice
import play.api.mvc._
import services.{SunService, WeatherService}

import scala.concurrent.ExecutionContext.Implicits.global
// import play.api.libs.ws.WSClient - no longer needed as injected in AppApplicationLoader


class Application (components: ControllerComponents, assets: Assets,
                   // Pass all dependencies as constructor parameters
                   sunService: SunService, weatherService: WeatherService)
    extends AbstractController(components) {

  def index: Action[AnyContent] = Action.async {
    val lat = -33.8830
    val lon = 151.2167
    val date = new java.util.Date()
    val dateTimeStr = new java.text.SimpleDateFormat("dd-mm-yyyy hh:mm:ss").format(date)

    for {
      sunInfo <- sunService.getSunInfo(lat, lon)
      temperature <- weatherService.getTemperature(lat, lon)
    } yield {
      Ok(views.html.index(dateTimeStr, sunInfo, temperature))
    }
  //  Future.successful { Ok(views.html.index(dateTimeStr, SunInfo("Rise", "Set", 99.9))) }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}

