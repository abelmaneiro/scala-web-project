import controllers.Application
import play.api.ApplicationLoader.Context
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Logger, LoggerConfigurator}
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.{DefaultControllerComponents, Filter}
import router.Routes
import play.api.routing.Router
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import actors.StatsActor
import akka.actor.{ActorRef, Props}
import filters.StatsFilter
import play.api
import play.api.db.{DBComponents, HikariCPComponents}
import play.api.db.evolutions.{DynamicEvolutions, EvolutionsComponents}
import play.filters.HttpFiltersComponents
import scalikejdbc.config.DBs
import services.{SunService, WeatherService}

import scala.concurrent.Future

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context): api.Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {loggerConfigurator =>
      loggerConfigurator.configure(context.environment)
    }
    new AppComponents(context).application
  }
}

// There are many built-in traits whose names end with Components such as AhcWSComponents, HikariCPComponents etc.
// They are meant to be added to your main AppComponents class when you need a particular built-in service.
// E.g. AhcWSComponents trait gives us an implementation of WSClient
class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) with AhcWSComponents
  with AssetsComponents with HttpFiltersComponents
  with EvolutionsComponents with DBComponents with HikariCPComponents {

  private val log = Logger(this.getClass)  // logger with this classname

  override lazy val controllerComponents: DefaultControllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController: Application = wire[Application]  // Application refers to ./app/controllers/Application
  // Wire in our classes
  lazy val sunService: SunService = wire[SunService]  // Use wire instead of new SunService(wsClient)
  lazy val weatherService: WeatherService = wire[WeatherService]
  // Wire HTTP filter and overriding the httpFilters field from the BuiltInComponentsFromContext class
  lazy val statsFilter: StatsFilter = wire[StatsFilter]
  override lazy val httpFilters: Seq[Filter] = Seq(statsFilter)  // Explicit type of Filter
  // BuiltInComponentsFromContext trait allows creation and management of Actors
  lazy val statsActor: ActorRef = actorSystem.actorOf(Props(wire[StatsActor]), StatsActor.name)
  //  Needed for Play Evolutions
  override lazy val dynamicEvolutions = new DynamicEvolutions
  // BuiltInComponentsFromContext trait allows adding a stop hook
  applicationLifecycle.addStopHook { () =>
//    Future.successful{ log.info("The app is stopping") }  //
    log.info("The app is about to stop")
    DBs.closeAll()   // start database
    Future.successful(())  // returns successful future of ()
  }

  val onStart: Unit = {  // Helps readability but don't really need to wrap in a val
    log.info("The app is about to start")
    applicationEvolutions
    DBs.setupAll()  // stop database
    statsActor ! StatsActor.Ping // starts the actor
  }
}