package eu.fakod.sjersey.inject.specs

import javax.ws.rs.QueryParam
import eu.fakod.sjersey.SJerseyTestBase
import com.sun.net.httpserver.HttpContext
import org.glassfish.jersey.server.ExtendedUriInfo
import com.sun.jndi.toolkit.ctx.ComponentContext
import eu.fakod.sjersey.inject.ScalaCollectionsQueryParamFactoryProvider
import org.glassfish.jersey.server.model.Parameter
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap

//class ScalaCollectionsQueryParamInjectableProviderSpec extends SJerseyTestBase {
//  "A Scala collections query param injectable provider" should {
//    val httpContext = mock[HttpContext]
//    val uriInfo = mock[ExtendedUriInfo]
//    val params = new MultivaluedStringMap()
//    params.add("name", "one")
//    params.add("name", "two")
//    params.add("name", "three")
//
//    httpContext.getUriInfo returns uriInfo
//    uriInfo.getQueryParameters(any[Boolean]) returns params
//
//    val context = mock[ComponentContext]
//    val queryParam = mock[QueryParam]
//
//    val provider = new ScalaCollectionsQueryParamFactoryProvider
//
//
//    "has a per-request scope" in {
//      provider.getScope.must(be(ComponentScope.PerRequest))
//    }
//
//    "returns an injectable for Seq instances" in {
//      val param = new Parameter(Array(), null, null, "name", null, classOf[Seq[String]], false, "default")
//      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]
//
//      injectable.getValue(httpContext) must be_==(Seq("one", "two", "three"))
//    }
//
//    "returns an injectable for List instances" in {
//      val param = new Parameter(Array(), null, null, "name", null, classOf[List[String]], false, "default")
//      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]
//
//      injectable.getValue(httpContext) must be_==(List("one", "two", "three"))
//    }
//
//    "returns an injectable for Vector instances" in {
//      val param = new Parameter(Array(), null, null, "name", null, classOf[Vector[String]], false, "default")
//      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]
//
//      injectable.getValue(httpContext) must be_==(Vector("one", "two", "three"))
//    }
//
//    "returns an injectable for IndexedSeq instances" in {
//      val param = new Parameter(Array(), null, null, "name", null, classOf[IndexedSeq[String]], false, "default")
//      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]
//
//      injectable.getValue(httpContext) must be_==(IndexedSeq("one", "two", "three"))
//    }
//
//    "return an injectable for Set instances" in {
//      val param = new Parameter(Array(), null, null, "name", null, classOf[Set[String]], false, "default")
//      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]
//
//      injectable.getValue(httpContext) must be_==(Set("one", "two", "three"))
//    }
//
//    "returns an injectable for Option instances" in {
//      val param = new Parameter(Array(), null, null, "name", null, classOf[Option[String]], false, "default")
//      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]
//
//      injectable.getValue(httpContext) must be_==(Some("one"))
//    }
//  }
//}
