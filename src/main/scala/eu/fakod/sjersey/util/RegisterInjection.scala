package eu.fakod.sjersey.util

import javax.servlet.{ServletContextEvent, ServletContextListener}
import org.glassfish.jersey.server.ResourceConfig
import eu.fakod.sjersey.inject.ParameterInjectionBinder
import scala.language.reflectiveCalls


class RegisterInjection extends ServletContextListener {


  /**
   * this is (Structural Type uses Reflection) because of a Scala Java interoperability issue
   */
  private def register(reg: { def register(o: Object): ResourceConfig }, o: Object) = reg.register(o)
  //sayer(new Ambig, Some(5))

  def contextInitialized(sce: ServletContextEvent): Unit = {
    //new ResourceConfig().register(new ParameterInjectionBinder())

    println("**********************************")
    register(new ResourceConfig(), new ParameterInjectionBinder())
  }

  def contextDestroyed(sce: ServletContextEvent): Unit = {}
}
