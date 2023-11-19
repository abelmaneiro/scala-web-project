package services

import model.SunInfo
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SunService(ws: WSClient) {
  def getSunInfo(latitude: Double, longitude: Double): Future[SunInfo] = {
    val sunResponseF = ws.url(s"https://api.sunrise-sunset.org/json?lat=$latitude&lng=$longitude&formatted=0").get()
    sunResponseF.map {sunResponse =>
      val sunJson = sunResponse.json
      val sunriseTimeStr = (sunJson \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (sunJson \ "results" \ "sunset").as[String]
      val sunriseTime = java.time.ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = java.time.ZonedDateTime.parse(sunsetTimeStr)
      val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss").
        withZone(java.time.ZoneId.of("Australia/Sydney"))
      val sunInfo = SunInfo(sunriseTime.format(formatter), sunsetTime.format(formatter))
      sunInfo
    }
  }


}
