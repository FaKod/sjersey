package eu.fakod.sjersey.util

import com.fasterxml.jackson.databind.{ObjectMapper, Module}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.{OutputStream, InputStream}
import java.lang.reflect.{ParameterizedType, Type}
import com.fasterxml.jackson.core.`type`.TypeReference

/**
 *
 */
trait JacksonDeAndSerializer {

  val module: Module = DefaultScalaModule

  def mapper = {
    val result = new ObjectMapper
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
      m.erasure
    }
    else new ParameterizedType {
      def getRawType = m.erasure

      def getActualTypeArguments = m.typeArguments.map(typeFromManifest).toArray

      def getOwnerType = null
    }
  }

}
