package eu.fakod.sjersey.integration.spec

import eu.fakod.sjersey.SJerseyTestBase
import eu.fakod.sjersey.providers.JacksonProvider
import javax.ws.rs._
import javax.ws.rs.client.{ClientBuilder, Entity}
import javax.ws.rs.core.MediaType
import org.glassfish.jersey.client.ClientConfig
import org.specs2.execute.{AsResult, Result, ResultExecution, Success}
import scala.util.Properties

case class SjerseyTest(s: String, i: Int, d: Double, b: Boolean)


class SJerseyIntegrationSpec extends SJerseyTestBase {

  implicit def unitAsResult = new AsResult[Unit] {
    override def asResult(r: => Unit): Result = ResultExecution.execute(r)(_ => Success())
  }

  val scalaShortVersion = Properties.versionNumberString.replaceAll("\\.[0-9]+$", "")

  "A Scala collection query param injectable with decoding" should {

    "extracts the query parameters" in {

      List("set", "list", "vector", "seq", "indexedset").foreach {
        settype =>
          val client = ClientBuilder.newClient
          val target = client.target("http://localhost:8080").path(s"sjersey_$scalaShortVersion/testresource/" + settype).
            queryParam("name", "1").queryParam("name", "2")
          val resp = target.request().get[String](classOf[String])
          resp must be_==("1, 2")
      }
    }
  }

  "A Scala MessageReaderWriterProvider" should {

    "extracts case classes" in {

      val clientConfig = new ClientConfig()
      clientConfig.register(classOf[JacksonProvider[_]])

      val client = ClientBuilder.newClient(clientConfig)
      val target = client.target("http://localhost:8080").path(s"sjersey_$scalaShortVersion/testresource/")

      val cc = SjerseyTest("1", 1, 2, true)
      val resp = target.request().post[SjerseyTest](Entity.entity(cc, MediaType.APPLICATION_JSON), classOf[SjerseyTest])
      resp must be_==(SjerseyTest("1received", 2, 3, false))
    }
  }
}


@Path("/testresource")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
class Things {
  @GET
  @Path("set")
  def getSet(@QueryParam("name") names: Set[String]) = names.mkString(", ")

  @GET
  @Path("list")
  def getList(@QueryParam("name") names: List[String]) = names.mkString(", ")

  @GET
  @Path("vector")
  def getVector(@QueryParam("name") names: Vector[String]) = names.mkString(", ")

  @GET
  @Path("seq")
  def getSeq(@QueryParam("name") names: Seq[String]) = names.mkString(", ")

  @GET
  @Path("indexedset")
  def getIndexedSeq(@QueryParam("name") names: IndexedSeq[String]) = names.mkString(", ")

  @POST
  def postCaseClass(cc: SjerseyTest) = SjerseyTest(cc.s + "received", cc.i + 1, cc.d + 1, !cc.b)
}
