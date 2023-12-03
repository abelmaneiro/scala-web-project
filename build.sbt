name := """scala-web-project"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.13.4"

libraryDependencies ++=Seq("com.typesafe.play" %% "play" % "2.8.4")
javaOptions += "-Dplay.editor=http://localhost:63342/api/file/?file=%s&line=%s"

lazy val root = (project in file(".")).enablePlugins(PlayScala)  // Enable Play
pipelineStages := Seq(digest)   // Making fingerprinting available

libraryDependencies ++= Seq(   //
  jdbc,
  caffeine,
  ws,
  evolutions,
  "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided", // marked as provided as only used at compile time
  "org.postgresql" % "postgresql" % "42.2.18",
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "de.svenkubiak" % "jBCrypt" % "0.4.1",
  //guice  // using macwire so removed.
)
//libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided" // marked as provided as only used at compile time & remove juice
//libraryDependencies += "org.postgresql" % "postgresql" % "42.2.18"
//libraryDependencies ++= Seq(
//  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
//  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
//  "ch.qos.logback" % "logback-classic" % "1.2.3",
//)
//libraryDependencies += "de.svenkubiak" % "jBCrypt" % "0.4.1"

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"  // where sbt looks for libraries. Not needed only included as an example
