import controllers.Application
import play.api.ApplicationLoader.Context
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Logger, LoggerConfigurator}
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.DefaultControllerComponents
import router.Routes
import play.api.routing.Router
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import services.{SunService, WeatherService}

import scala.concurrent.Future

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context) = {
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
  with AssetsComponents with HttpFiltersComponents {

  private val log = Logger(this.getClass)  // logger with this classname

  override lazy val controllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController = wire[Application]  // Application refers to ./app/controllers/Application
  // Wire in our classes
  lazy val sunService = wire[SunService]  // Use wire instead of new SunService(wsClient)
  lazy val weatherService = wire[WeatherService]

  // BuiltInComponentsFromContext trait allows adding a stop hook
  applicationLifecycle.addStopHook { () =>
//    Future.successful{ log.info("The app is stopping") }  //
    log.info("The app is about to stop")
    Future.successful(())  // returns successful future of ()
  }

  val onStart = {  // Helps readability but don't really need to wrap in a val
    log.info("The app is about to start")
  }
}