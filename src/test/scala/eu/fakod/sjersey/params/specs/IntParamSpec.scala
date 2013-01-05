package eu.fakod.sjersey.params.specs

import eu.fakod.sjersey.params.IntParam
import javax.ws.rs.WebApplicationException
import eu.fakod.sjersey.SJerseyTestBase

class IntParamSpec extends SJerseyTestBase {
  "A valid int parameter" should {
    val param = IntParam("40")

    "has an int value" in {
      param.value must be_==(40)
    }
  }

  "An invalid int parameter" should {
    "throws a WebApplicationException with an error message" in {

      IntParam("poop") must throwA[WebApplicationException].like {
        case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must be_==(400)
          response.getEntity must be_==("Invalid parameter: poop (Must be an integer value.)")
      }

    }
  }
}
