package eu.fakod.sjersey.inject.specs

import eu.fakod.sjersey.inject.ScalaOptionStringExtractor
import eu.fakod.sjersey.SJerseyTestBase
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap

class ScalaOptionStringExtractorSpec extends SJerseyTestBase {
  "Extracting a parameter" should {
    val extractor = new ScalaOptionStringExtractor("name", "default")

    "has a name" in {
      extractor.getName.must(be("name"))
    }

    "has a default value" in {
      extractor.getDefaultValueString.must(be("default"))
    }

    "extracts the first of a set of parameter values" in {
      val params = new MultivaluedStringMap()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result must be_==(Some("one"))
    }

    "uses the default value if no parameter exists" in {
      val params = new MultivaluedStringMap()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result must be_==(Some("default"))
    }
  }

  "Extracting a parameter with no default value" should {
    val extractor = new ScalaOptionStringExtractor("name", null)

    "returns None" in {
      val params = new MultivaluedStringMap()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result must be_==(None)
    }
  }
}
