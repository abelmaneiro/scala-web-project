package model

import play.api.libs.json.Json

case class CombinedData (sunInfo: SunInfo, temperature: Double, noRequests: Int)

object CombinedData {
  implicit val write = Json.writes[CombinedData]
}