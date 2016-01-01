package eu.fakod.sjersey.inject

import eu.fakod.sjersey.inject.ScalaCollectionsFactoryProvider.{FormParamValueFactory, HeaderParamValueFactory, QueryParamValueFactory}
import javax.inject.{Inject, Singleton}
import javax.ws.rs.core.Form
import javax.ws.rs.{FormParam, HeaderParam, ProcessingException, QueryParam}
import org.glassfish.hk2.api.{InjectionResolver, ServiceLocator, TypeLiteral}
import org.glassfish.hk2.utilities.binding.AbstractBinder
import org.glassfish.jersey.internal.inject.ExtractorException
import org.glassfish.jersey.server.ParamException
import org.glassfish.jersey.server.internal.inject._
import org.glassfish.jersey.server.model.Parameter
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider

/**
 * Object as "container" for former static Java classes
 */
object ScalaCollectionsFactoryProvider {

  class QueryParamInjectionResolver extends ParamInjectionResolver[QueryParam](classOf[ScalaCollectionsQueryParamFactoryProvider]) {}

  class HeaderParamInjectionResolver extends ParamInjectionResolver[HeaderParam](classOf[ScalaCollectionsHeaderParamFactoryProvider]) {}

  class FormParamInjectionResolver extends ParamInjectionResolver[FormParam](classOf[ScalaCollectionsFormParamFactoryProvider]) {}

  class QueryParamValueFactory(extractor: MultivaluedParameterExtractor[_], decode: Boolean)
    extends AbstractContainerRequestValueFactory[AnyRef] {

    def provide: AnyRef = try {
      extractor.extract(getContainerRequest.getUriInfo.getQueryParameters(decode)).asInstanceOf[AnyRef]
    } catch {
      case e: ExtractorException =>
        throw new ParamException.QueryParamException(e.getCause, extractor.getName, extractor.getDefaultValueString)
    }
  }

  class FormParamValueFactory(extractor: MultivaluedParameterExtractor[_])
    extends AbstractContainerRequestValueFactory[AnyRef] {
    override def provide(): AnyRef = try {
      getContainerRequest.bufferEntity()
      val form = getContainerRequest.readEntity(classOf[Form])
      extractor.extract(form.asMap()).asInstanceOf[AnyRef]
    } catch {
      case e: ProcessingException =>
        throw new ParamException.FormParamException(e.getCause, extractor.getName, extractor.getDefaultValueString)
    }
  }

  class HeaderParamValueFactory(extractor: MultivaluedParameterExtractor[_])
    extends AbstractContainerRequestValueFactory[AnyRef] {
    override def provide(): AnyRef = try {
      extractor.extract(getContainerRequest.getHeaders).asInstanceOf[AnyRef]
    } catch {
      case e: ExtractorException =>
        throw new ParamException.HeaderParamException(e.getCause, extractor.getName, extractor.getDefaultValueString)
    }
  }

}

/**
 * A parameter value factory provider that provides parameter value factories
 * which are using MultivaluedParameterExtractorProvider to extract parameter
 * values from the supplied MultivaluedMap multivalued parameter map.
 **/
class ScalaCollectionsFactoryProvider @Inject()(mpep: MultivaluedParameterExtractorProvider,
                                                locator: ServiceLocator,
                                                source: Parameter.Source,
                                                valueFactoryFactory: (MultivaluedParameterExtractor[_], Boolean) => AbstractContainerRequestValueFactory[_])
  extends AbstractValueFactoryProvider(mpep, locator, source) {

  def createValueFactory(parameter: Parameter): AbstractContainerRequestValueFactory[_] =
    parameter.getSourceName match {
      case parameterName if parameterName != null && parameterName.length() != 0 =>
        Option(buildExtractor(parameterName, parameter.getDefaultValue, parameter.getRawType)).
          map(valueFactoryFactory(_, !parameter.isEncoded)).orNull
      case _ => null
    }

  private def buildExtractor(name: String, default: String, klass: Class[_]): MultivaluedParameterExtractor[_] = {
    if (klass == classOf[Seq[String]]) {
      new ScalaCollectionStringReaderExtractor[Seq](name, default, Seq)
    } else if (klass == classOf[List[String]]) {
      new ScalaCollectionStringReaderExtractor[List](name, default, List)
    } else if (klass == classOf[Vector[String]]) {
      new ScalaCollectionStringReaderExtractor[Vector](name, default, Vector)
    } else if (klass == classOf[IndexedSeq[String]]) {
      new ScalaCollectionStringReaderExtractor[IndexedSeq](name, default, IndexedSeq)
    } else if (klass == classOf[Set[String]]) {
      new ScalaCollectionStringReaderExtractor[Set](name, default, Set)
    } else if (klass == classOf[Option[String]]) {
      new ScalaOptionStringExtractor(name, default)
    } else null
  }
}

class ScalaCollectionsQueryParamFactoryProvider @Inject()(mpep: MultivaluedParameterExtractorProvider, locator: ServiceLocator)
  extends ScalaCollectionsFactoryProvider(mpep, locator, Parameter.Source.QUERY, { (extractor, decode) =>
    new QueryParamValueFactory(extractor, decode)
  })

class ScalaCollectionsHeaderParamFactoryProvider @Inject()(mpep: MultivaluedParameterExtractorProvider, locator: ServiceLocator)
  extends ScalaCollectionsFactoryProvider(mpep, locator, Parameter.Source.HEADER, { (extractor, _) =>
    new HeaderParamValueFactory(extractor)
  })

class ScalaCollectionsFormParamFactoryProvider @Inject()(mpep: MultivaluedParameterExtractorProvider, locator: ServiceLocator)
  extends ScalaCollectionsFactoryProvider(mpep, locator, Parameter.Source.FORM, { (extractor, _) =>
    new FormParamValueFactory(extractor)
  })

/**
 * binds the respective Provider to this Application
 */
class ParameterInjectionBinder extends AbstractBinder {
  def configure(): Unit = {
    bind(classOf[ScalaCollectionsQueryParamFactoryProvider]).to(classOf[ValueFactoryProvider]).in(classOf[Singleton])
    bind(classOf[ScalaCollectionsHeaderParamFactoryProvider]).to(classOf[ValueFactoryProvider]).in(classOf[Singleton])
    bind(classOf[ScalaCollectionsFormParamFactoryProvider]).to(classOf[ValueFactoryProvider]).in(classOf[Singleton])

    bind(classOf[ScalaCollectionsFactoryProvider.QueryParamInjectionResolver]).
      to(new TypeLiteral[InjectionResolver[QueryParam]]() {
      }).in(classOf[Singleton])
    bind(classOf[ScalaCollectionsFactoryProvider.HeaderParamInjectionResolver]).
      to(new TypeLiteral[InjectionResolver[HeaderParam]]() {
      }).in(classOf[Singleton])
    bind(classOf[ScalaCollectionsFactoryProvider.FormParamInjectionResolver]).
      to(new TypeLiteral[InjectionResolver[FormParam]]() {
      }).in(classOf[Singleton])
  }
}
