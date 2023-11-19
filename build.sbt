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
  "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided" // marked as provided as only used at compile time
//guice  // using macwire so removed.
)
//libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided" // marked as provided as only used at compile time & remove juice

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"  // where sbt looks for libraries. Not needed only included as an example
