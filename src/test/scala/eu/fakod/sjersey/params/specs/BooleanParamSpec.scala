package eu.fakod.sjersey.params.specs

import eu.fakod.sjersey.params.BooleanParam
import javax.ws.rs.WebApplicationException
import eu.fakod.sjersey.SJerseyTestBase

class BooleanParamSpec extends SJerseyTestBase {
  "A valid boolean parameter" should {
    val param = BooleanParam("true")

    "has a boolean value" in {
      param.value must beTrue
    }
  }

  "An invalid boolean parameter" should {
    "throws a WebApplicationException with an error message" in {

      BooleanParam("poop") must throwA[WebApplicationException].like {
        case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must be_==(400)
          response.getEntity must be_==("Invalid parameter: poop (Must be \"true\" or \"false\".)")
      }

    }
  }
}
