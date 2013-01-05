package eu.fakod.sjersey.params.specs

import eu.fakod.sjersey.params.LongParam
import javax.ws.rs.WebApplicationException
import eu.fakod.sjersey.SJerseyTestBase

class LongParamSpec extends SJerseyTestBase {
  "A valid long parameter" should {
    val param = LongParam("40")

    "has an int value" in {
      param.value must be_==(40L)
    }
  }

  "An invalid long parameter" should {
    "throws a WebApplicationException with an error message" in {

      LongParam("poop") must throwA[WebApplicationException].like {
        case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must be_==(400)
          response.getEntity must be_==("Invalid parameter: poop (Must be an integer value.)")
      }

    }
  }
}
