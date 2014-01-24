SJersey
============

*Life's too short to use `java.util.Collection`*

SJersey is a set of classes which add Scala interoperation to Jersey.
It is intended to be a replacement for jersey-scala.
Instead of Jerkson, jackson-module-scala (Jackson JSON) is used now.


##Versions:

### sjersey_2.10 0.4.1-SNAPSHOT
* Removed unneeded code from trait JacksonDeAndSerializer

### sjersey_2.10 0.4.0
* bumped Jersey Version to 2.5.1
* bumped jackson-module-scala to 2.3.1

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
    <version>0.4.0</version>
</dependency>
```

**Second**, configure the servlet (in case of using one):

```xml

<servlet>
    <servlet-name>sjersey-service</servlet-name>
    <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>

    <init-param>
        <param-name>javax.ws.rs.Application</param-name>
        <param-value>eu.fakod.sjersey.util.RegisterParameterInjectionBinder</param-value>
    </init-param>

    <init-param>
        <param-name>jersey.config.server.provider.packages</param-name>
        <param-value>my.resources.package;eu.fakod.sjersey.providers</param-value>
    </init-param>
</servlet>

```

**Third**, write your resource classes:

```scala
case class FooCC(s: String, i: Int, d: Double, b: Boolean)
case class BarCC(s: String, i: Int)

@Path("/things")
@Consumes(Array("application/json"))
@Produces(Array("application/json"))
class Things {
  @GET
  def getAThing(@QueryParam("name") names: Set[String]) = "I found: " + names.mkString(", ")
  
  @POST
  def postAThing(cc: FooCC) = BarCC("received", cc.i)
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
