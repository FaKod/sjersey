SJersey
============

*Life's too short to use `java.util.Collection`*

SJersey is a set of classes which add Scala interoperation to Jersey.


Requirements
------------

* Scala 2.9.2
* jackson-module-scala 2.1.3
* Jersey 1.9.1
* Slf4j API 1.6.2


How To Use
----------

**First**, specify Jersey-Scala as a dependency:

```xml
<dependency>
    <groupId>eu.fakod</groupId>
    <artifactId>sjersey${scala.version}</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Second**, write your resource classes:

```scala
@Path("/things")
@Produces("text/plain")
class Things {
  @GET
  def getAThing(@QueryParam("name") names: Set[String]) = "I found: " + names.mkString(", ")
}
```
    


What All This Supports (subject to change)
----------------------

* `QueryParam`-annotated parameters of type `Seq[String]`, `List[String]`,
  `Vector[String]`, `IndexedSeq[String]`, `Set[String]`, and `Option[String]`.
* `AST.JValue` request and response entities.
* `JsonNode` request and response entities.
* Case class (i.e., `Product` instances) JSON request and response entities.
* `Array[A]` request and response entities. (Due to the JVM's type erasure and
  mismatches between Scala and Java type signatures, this is the only "generic"
  class supported since `Array` type parameters are reified.)
