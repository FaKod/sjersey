package eu.fakod.sjersey.inject.specs

import eu.fakod.sjersey.inject.ScalaCollectionStringReaderExtractor
import eu.fakod.sjersey.SJerseyTestBase
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap

class ScalaCollectionStringReaderExtractorSpec extends SJerseyTestBase {
  "Extracting a parameter" should {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", "default", Set)

    "has a name" in {
      extractor.getName.must(be("name"))
    }

    "has a default value" in {
      extractor.getDefaultValueString must be_==("default")
    }

    "extracts a set of parameter values" in {
      val params = new MultivaluedStringMap()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must be_==(Set("one", "two", "three"))
    }

    "uses the default value if no parameter exists" in {
      val params = new MultivaluedStringMap()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must be_==(Set("default"))
    }
  }

  "Extracting a parameter with no default value" should {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", null, Set)

    "returns an empty collection" in {
      val params = new MultivaluedStringMap()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must be_==(Set.empty[String])
    }
  }
}
