package eu.fakod.sjersey.util

import com.fasterxml.jackson.databind.{MappingJsonFactory, ObjectMapper, Module}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.{OutputStream, InputStream}
import java.lang.reflect.{ParameterizedType, Type}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.core.{JsonParser, JsonGenerator}

/**
 * uses jackson-module-scala to de- and serialize Json
 */
trait JacksonDeAndSerializer {

  val module: Module = DefaultScalaModule

  val mapper = {
    val factory = new MappingJsonFactory
    factory.enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)
    factory.enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
    factory.enable(JsonGenerator.Feature.QUOTE_FIELD_NAMES)
    factory.enable(JsonParser.Feature.ALLOW_COMMENTS)
    factory.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE)

    val result = new ObjectMapper(factory)
    result.registerModule(module)
    result
  }

  def deserialize[T: Manifest](value: InputStream): T =
    mapper.readValue(value, typeReference[T])

  def deserialize[T: Manifest](value: String): T =
    mapper.readValue(value, typeReference[T])

  def serialize(value: Any, writer: OutputStream): Unit =
    mapper.writeValue(writer, value)

  def serialize(value: Any): String =
    mapper.writeValueAsString(value)

  private[this] def typeReference[T: Manifest] = new TypeReference[T] {
    override def getType = typeFromManifest(manifest[T])
  }

  private[this] def typeFromManifest(m: Manifest[_]): Type = {
    if (m.typeArguments.isEmpty) {
      m.runtimeClass
    }
    else new ParameterizedType {
      def getRawType = m.runtimeClass

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }

}
