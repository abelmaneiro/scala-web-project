package service

import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WeatherService(ws: WSClient) {
  def getTemperature(latitude: Double, longitude: Double): Future[Double] = {
    // To obtain api key - https://openweathermap.org/appid
    // https://api.openweathermap.org/data/2.5/weather?lat=-33.8830&lon=151.2167&unit&appid=d06f9fa75ebe72262aa71dc6c1dcd118&units=metric
    val weatherResponseF = ws.url("https://api.openweathermap.org/data/2.5/weather?" +
      s"lat=$latitude&lon=$longitude&unit&appid=d06f9fa75ebe72262aa71dc6c1dcd118&units=metric").get()
    weatherResponseF.map { weatherResponse =>
      val weatherJSon = weatherResponse.json
      val temperature = (weatherJSon \ "main" \ "temp").as[Double]
      temperature
    }
  }
}
