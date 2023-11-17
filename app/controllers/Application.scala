package controllers

import controllers.Assets.Asset

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
// import scala.concurrent.Future
import play.api.libs.ws.WSClient
import model.SunInfo


class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  def index: Action[AnyContent] = Action.async {
    val date = new java.util.Date()
    val dateStr = new java.text.SimpleDateFormat().format(date)
    val sunResponseF = ws.url("https://api.sunrise-sunset.org/json?lat=-33.8830&lng=151.2167&formatted=0").get()
    // To obtain api key - https://openweathermap.org/appid
    // https://api.openweathermap.org/data/2.5/weather?lat=-33.8830&lon=151.2167&unit&appid=d06f9fa75ebe72262aa71dc6c1dcd118&units=metric
    val weatherResponseF = ws.url("https://api.openweathermap.org/data/2.5/weather?" +
      "lat=-33.8830&lon=151.2167&unit&appid=d06f9fa75ebe72262aa71dc6c1dcd118&units=metric").get()
    for {
      sunResponse <- sunResponseF
      weatherResponse <- weatherResponseF
    } yield {
      val weatherJSon = weatherResponse.json
      val temp = (weatherJSon \ "main" \ "temp").as[Double]
      val sunJson = sunResponse.json
      val sunriseTimeStr = (sunJson \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (sunJson \ "results" \ "sunset").as[String]
      val sunriseTime = java.time.ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = java.time.ZonedDateTime.parse(sunsetTimeStr)
      val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss").
        withZone(java.time.ZoneId.of("Australia/Sydney"))
      val sunInfo = SunInfo(sunriseTime.format(formatter), sunsetTime.format(formatter))
      Ok(views.html.index(dateStr, sunInfo, temp))
    }
  //  Future.successful { Ok(views.html.index(dateStr, SunInfo("Rise", "Set"))) }
  }

  def versioned(path: String, file: Asset): Action[AnyContent] = assets.versioned(path, file)
}

