package controllers

import controllers.Assets.Asset

import javax.inject._
import play.api.mvc._
import service.{SunService, WeatherService}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.ws.WSClient

class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {

  private val sunService = new SunService(ws)
  private val weatherService = new WeatherService(ws)
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

