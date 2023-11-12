name := """scala-web-project"""
version := "1.0-SNAPSHOT"
scalaVersion := "2.13.4"

lazy val root = (project in file(".")).enablePlugins(PlayScala)  // Enable Play
pipelineStages := Seq(digest)   // Making fingerprinting available

libraryDependencies ++= Seq(   //
  jdbc,
  caffeine,
  ws,
  guice
)

resolvers += "sonatype-snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"  // where sbt looks for libraries. Not needed only included as an example
