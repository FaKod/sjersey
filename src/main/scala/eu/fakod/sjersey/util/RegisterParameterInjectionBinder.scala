package eu.fakod.sjersey.util

import org.glassfish.jersey.server.ResourceConfig
import eu.fakod.sjersey.inject.ParameterInjectionBinder
import scala.language.reflectiveCalls

/**
 * the parameter injection can't IMHO be registered with a Provider annotation
 * so add this class to the init parameter section of the web.xml, like
 * {{{
 * <init-param>
 * <param-name>javax.ws.rs.Application</param-name>
 * <param-value>eu.fakod.sjersey.util.RegisterInjection</param-value>
 * </init-param>
 * }}}
 */
class RegisterParameterInjectionBinder extends ResourceConfig {

  _register(this, new ParameterInjectionBinder())

  /**
   * this uses Structural Type (uses Reflection) because of a Scala Java interoperability issue
   */
  private def _register(reg: {def register(o: Object): ResourceConfig}, o: Object) = reg.register(o)
}