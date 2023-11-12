package controllers

import controllers.Assets.Asset

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.ws.WSClient
import model.SunInfo


class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  def index = Action.async {
    val date = new java.util.Date()
    val dateStr = new java.text.SimpleDateFormat().format(date)
    val responseF = ws.url("http://api.sunrise-sunset.org/json?lat=-33.8830&lng=151.2167&formatted=0").get()
    responseF.map {response =>
      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunInfo = SunInfo(sunriseTimeStr, sunsetTimeStr)
      Ok(views.html.index(dateStr, sunInfo))
    }
  //  Future.successful { Ok(views.html.index(dateStr, SunInfo("Rise", "Set"))) }

  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
