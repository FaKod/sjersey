SJersey
============

*Life's too short to use `java.util.Collection`*

SJersey is a set of classes which add Scala interoperation to Jersey.


##Versions:

### sjersey_2.10 0.3.3
* bumped to Scala 2.10.3 and deployed to Central
* bumped versions for jersey-server and jackson-module-scala


How To Use
----------

**First**, specify Jersey-Scala as a dependency:

```xml
<dependency>
    <groupId>eu.fakod</groupId>
    <artifactId>sjersey_2.10</artifactId>
    <version>0.3.3</version>
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
    


What All This Supports (subject to change, resp. not all is validated)
----------------------

* `QueryParam`-annotated parameters of type `Seq[String]`, `List[String]`,
  `Vector[String]`, `IndexedSeq[String]`, `Set[String]`, and `Option[String]`.
* `JsonNode` request and response entities.
* Case class (i.e., `Product` instances) JSON request and response entities.
* `Array[A]` request and response entities. (Due to the JVM's type erasure and
  mismatches between Scala and Java type signatures, this is the only "generic"
  class supported since `Array` type parameters are reified.)
