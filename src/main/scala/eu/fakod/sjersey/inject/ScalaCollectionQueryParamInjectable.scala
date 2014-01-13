package eu.fakod.sjersey.inject

import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractor
import com.sun.net.httpserver.HttpContext
import org.glassfish.jersey.server.ParamException


//class ScalaCollectionQueryParamInjectable(extractor: MultivaluedParameterExtractor,
//                                          decode: Boolean)
//        extends AbstractHttpContextInjectable[Object] {
//
//  def getValue(c: HttpContext) = try {
//    extractor.extract(c.getUriInfo.getQueryParameters(decode))
//  } catch {
//    case e: ExtractorContainerException =>
//      throw new ParamException.QueryParamException(e.getCause,
//                                                   extractor.getName,
//                                                   extractor.getDefaultStringValue)
//  }
//}
