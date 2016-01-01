name := "sjersey"

version := "0.4.4-SNAPSHOT"

organization := "eu.fakod"

crossScalaVersions := Seq("2.11.7", "2.10.4")


libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.5.1"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.13" % "test"

libraryDependencies += "org.glassfish.jersey.core" %% "jersey-server" % "2.21" cross CrossVersion.Disabled

libraryDependencies += "org.glassfish.jersey.containers" %% "jersey-container-servlet" % "2.21" cross CrossVersion.Disabled

libraryDependencies += "org.slf4j" %% "slf4j-api" % "1.6.2" cross CrossVersion.Disabled

libraryDependencies += "org.mockito" %% "mockito-all" % "1.9.0" % "test" cross CrossVersion.Disabled

libraryDependencies += "org.slf4j" %% "slf4j-nop" % "1.6.2" % "test" cross CrossVersion.Disabled

libraryDependencies += "junit" %% "junit" % "4.7" % "test" cross CrossVersion.Disabled



//enablePlugins(TomcatPlugin)

sourceDirectory in webappPrepare := (sourceDirectory in Test).value / "WebContent"

containerForkOptions := new ForkOptions(runJVMOptions = Seq("-Dh2g2=42"))

webappPostProcess := {
  webappDir: File =>
    val baseDir = baseDirectory.value / "src" / "test"
    IO.copyDirectory(baseDir / "webapp", webappDir)
}


publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/FaKod/sjersey</url>
    <licenses>
      <license>
        <name>the Apache License, ASL Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <developers>
      <developer>
        <id>FaKod</id>
        <name>Christopher Schmidt</name>
        <timezone>+1</timezone>
        <email>info [at] FaKod.EU</email>
        <roles>
          <role>developer</role>
        </roles>
      </developer>
    </developers>
    <scm>
      <developerConnection>scm:git:git@github.com:FaKod/sjersey.git</developerConnection>
      <url>git@github.com:FaKod/sjersey.git</url>
    </scm>
  )





