package eu.fakod.sjersey.providers.specs


import eu.fakod.sjersey.util.JacksonDeAndSerializer
import eu.fakod.sjersey.SJerseyTestBase


abstract class Meat

abstract class Cattle

case class Schwein(wild: Boolean) extends Cattle

case class Rind(name: String) extends Cattle

case class Wurst(sizeInCm: Int, wurstType: String) extends Meat

case class Steak(baseCattle: Cattle) extends Meat

class DeSerializerTest extends SJerseyTestBase with JacksonDeAndSerializer {
  sequential

  "Deserializing a simple class" should {

    "result in the right Wurst" in {
      val wurstJson =
        """{"sizeInCm": 20,"wurstType" : "Salami"}"""

      val wurst = deserialize[Wurst](wurstJson)
      wurst.sizeInCm should equalTo(20)
    }
  }

  "Deserializing typed containers" should {
    "be typed correctly and also contain the right stuff" in {

      val data: Array[Array[Wurst]] = Array(Array(Wurst(30, "Mortadella")))
      val json = serialize(data)
      val wursts = deserialize[Array[Array[Wurst]]](json)

      wursts.size should equalTo(1)
      wursts(0).size should equalTo(1)
      wursts(0)(0).sizeInCm should equalTo(30)
    }
  }

  "Deserializing polymorphic classes" should {
    "be typed correctly and also contain the right stuff" in {

      val typedSer = new JacksonDeAndSerializer {
        mapper.enableDefaultTyping()
      }

      val json =
        """[[["eu.fakod.sjersey.providers.specs.Wurst",{"sizeInCm":30,"wurstType":"Mortadella"}]]]"""

      val wursts = typedSer.deserialize[Array[Array[Meat]]](json)

      wursts(0)(0).isInstanceOf[Wurst] should equalTo(true)
    }
  }

  "Deserializing polymorphic array contents" should {
    "be typed correctly and also contain the right stuff" in {

      val typedSer = new JacksonDeAndSerializer {
        mapper.enableDefaultTyping()
      }

      val json =
        """[[
          |["eu.fakod.sjersey.providers.specs.Steak",{"baseCattle":["eu.fakod.sjersey.providers.specs.Schwein",{"wild":true}]}],
          |["eu.fakod.sjersey.providers.specs.Steak",{"baseCattle":["eu.fakod.sjersey.providers.specs.Rind",{"name":"Emma"}]}],
          |["eu.fakod.sjersey.providers.specs.Wurst",{"sizeInCm":30,"wurstType":"Mortadella"}]
          |]]""".stripMargin

      val wursts = typedSer.deserialize[Array[Array[Meat]]](json)

      wursts(0)(0).asInstanceOf[Steak].baseCattle.asInstanceOf[Schwein].wild must equalTo(true)
      wursts(0)(1).asInstanceOf[Steak].baseCattle.asInstanceOf[Rind].name must equalTo("Emma")
      wursts(0)(2).asInstanceOf[Wurst].sizeInCm must equalTo(30)
    }
  }
}
