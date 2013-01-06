package eu.fakod.sjersey.providers.specs

import javax.ws.rs.core.MediaType
import eu.fakod.sjersey.providers.JacksonProvider
import javax.ws.rs.WebApplicationException
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}
import eu.fakod.sjersey.SJerseyTestBase

class JacksonProviderSpec extends SJerseyTestBase {
  private val provider = new JacksonProvider[Array[Int]]

  "An array of ints" should {
    "is writable" in {
      provider.isWriteable(Array.empty[Int].getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }

    "is readable" in {
      provider.isReadable(Array.empty[Int].getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }
  }

  "Parsing an application/json request entity" should {
    val entity = new ByteArrayInputStream("[1, 2, 3]".getBytes)

    "returns an array of the given type" in {
      provider.readFrom(classOf[Array[Int]], null, null, null, null, entity) must be_==(Array(1, 2, 3))
    }
  }

  "Parsing an malformed application/json request entity" should {
    val entity = new ByteArrayInputStream("[1, 2".getBytes)

    "throws a 400 Bad Request WebApplicationException" in {

      provider.readFrom(classOf[Array[Int]], null, null, null, null, entity) must throwA[WebApplicationException].like {
        case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must be_==(400)
          response.getEntity.toString must startWith("Unexpected end-of-input: expected close marker for ARRAY")
      }

    }
  }

  "Rendering an application/json response entity" should {
    "produces a compact JSON array" in {
      val output = new ByteArrayOutputStream
      provider.writeTo(Array(1, 2, 3), null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)

      output.toString must be_==("[1,2,3]")
    }
  }
}
