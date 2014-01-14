package eu.fakod.sjersey.integration.spec

import eu.fakod.sjersey.SJerseyTestBase
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.{Path, Produces, GET, QueryParam}


class ScalaCollectionQueryParamInjectableIntegrationSpec extends SJerseyTestBase {

  "A Scala collection query param injectable with decoding" should {

    "extracts the query parameters" in {

      val client = ClientBuilder.newClient
      val target = client.target("http://localhost:8080").path("sjersey_2.10/application.wadl")//.path("sjersey-service/things")

      val resp = target.request().get[String](classOf[String])

      println("Response: " + resp)

      success
    }
  }

}

@Path("/things")
@Produces(Array("text/plain"))
class Things {
  @GET
  def getAThing(@QueryParam("name") names: Set[String]) = "I found: " + names.mkString(", ")
}
