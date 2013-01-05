package eu.fakod.sjersey.providers

import javax.ws.rs.ext.Provider
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import java.lang.reflect.{ParameterizedType, Type}
import java.lang.annotation.Annotation
import java.io.{IOException, InputStream, OutputStream}
import org.slf4j.LoggerFactory
import javax.ws.rs.{WebApplicationException, Consumes, Produces}
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import javax.ws.rs.core.Response.Status
import scala.reflect.Manifest
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.{Module, ObjectMapper}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.core.JsonParseException

/**
 *
 */
trait DeAndSerializer {

  def module: Module

  def mapper = {
    val result = new ObjectMapper
    result.registerModule(module)
    result
  }

  def deserialize[T: Manifest](value: InputStream): T =
    mapper.readValue(value, typeReference[T])

  def serialize(value: Any, writer: OutputStream): Unit =
    mapper.writeValue(writer, value)

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

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
class JerksonProvider[A] extends AbstractMessageReaderWriterProvider[A] with DeAndSerializer {

  private val logger = LoggerFactory.getLogger(classOf[JerksonProvider[_]])

  lazy val module = DefaultScalaModule

  def readFrom(klass: Class[A],
               genericType: Type,
               annotations: Array[Annotation],
               mediaType: MediaType,
               httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream) = {
    try {
      deserialize(entityStream)(Manifest.classType(klass))
    } catch {
      case e: JsonParseException => {
        throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(e.getMessage)
          .build)
      }
    }
  }

  def isReadable(klass: Class[_],
                 genericType: Type,
                 annotations: Array[Annotation],
                 mediaType: MediaType) = mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)

  def writeTo(t: A,
              klass: Class[_],
              genericType: Type,
              annotations: Array[Annotation],
              mediaType: MediaType,
              httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    try {
      serialize(t, entityStream)
    } catch {
      case e: IOException => logger.debug("Error writing to stream", e)
      case e => logger.error("Error encoding %s as JSON".format(t, e))
    }
  }

  def isWriteable(klass: Class[_],
                  genericType: Type,
                  annotations: Array[Annotation],
                  mediaType: MediaType) = mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE)
}
